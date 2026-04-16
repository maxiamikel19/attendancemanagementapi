package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.entity.Box;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketPriority;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketStatus;
import com.github.maxiamikel.attendancemanagementapi.exceptions.BusinessException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ResourceNotFoundException;
import com.github.maxiamikel.attendancemanagementapi.repository.TicketRepository;
import com.github.maxiamikel.attendancemanagementapi.services.AttendanceService;
import com.github.maxiamikel.attendancemanagementapi.services.DepartmentService;
import com.github.maxiamikel.attendancemanagementapi.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final DepartmentService departmentService;

    private static final int MAX_RECALLS = 4;
    private static final List<TicketStatus> ACTIVE_STATUSES = List.of(TicketStatus.CALLED, TicketStatus.ATTENDING);

    @Override
    @Transactional
    public Ticket callNextTicket(UUID userId) {

        User operator = getValidOperator(userId);
        ensureNoActiveTicket(operator);

        Ticket ticket = ticketRepository
                .callNextTicketAtomic(
                        operator.getDepartment().getId(),
                        TicketStatus.WAITING.name(),
                        operator.getBox().getId())
                .orElseThrow(() -> new ResourceNotFoundException("No tickets available"));

        log.info("Ticket {} taken by user {}", ticket.getPassCode(), operator.getName());

        return ticket;
    }

    @Override
    @Transactional
    public Ticket callNextTicketByPriority(TicketPriority priority, UUID userId) {

        User operator = getValidOperator(userId);
        ensureNoActiveTicket(operator);

        return ticketRepository
                .callNextTicketByPriority(
                        operator.getDepartment().getId(),
                        priority.name(),
                        operator.getBox().getId())
                .orElseThrow(() -> new BusinessException("No tickets available"));
    }

    @Override
    @Transactional
    public Ticket recallTicket(UUID userId) {

        User operator = getValidOperator(userId);
        Ticket ticket = getTicketByBoxAndStatus(operator.getBox(), TicketStatus.CALLED);

        ticket.setRecallCount(ticket.getRecallCount() + 1);

        if (ticket.getRecallCount() >= MAX_RECALLS) {
            updateStatus(ticket, TicketStatus.CANCELLED);
        }

        log.info("Ticket {} recalled at box {} for {} times", ticket.getPassCode(), operator.getBox().getBoxNumber(),
                ticket.getRecallCount());

        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Ticket startTicket(UUID userId) {

        User operator = getValidOperator(userId);
        Ticket ticket = getTicketByBoxAndStatus(operator.getBox(), TicketStatus.CALLED);

        updateStatus(ticket, TicketStatus.ATTENDING);

        log.info("Ticket {} started attendance at box {}", ticket.getPassCode(),
                operator.getBox().getBoxNumber());

        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Ticket finalizeTicket(UUID userId) {

        User operator = getValidOperator(userId);
        Ticket ticket = getTicketByBoxAndStatus(operator.getBox(), TicketStatus.ATTENDING);

        updateStatus(ticket, TicketStatus.FINALIZED);

        log.info("Ticket {} finished attendance at box {}", ticket.getPassCode(),
                operator.getBox().getBoxNumber());

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getCurrentTicketTicket(UUID userId) {

        User operator = getValidOperator(userId);

        return ticketRepository.findFirstByBoxAndTicketStatusIn(operator.getBox(), ACTIVE_STATUSES)
                .orElseThrow(() -> new ResourceNotFoundException("No active ticket"));

    }

    @Override
    @Transactional
    public Ticket transferTicket(UUID departmentId, UUID userId) {

        User operator = getValidOperator(userId);

        Ticket ticket = ticketRepository.findFirstByBoxAndTicketStatus(operator.getBox(), TicketStatus.ATTENDING)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No active ticket ATTENDING"));

        Department department = departmentService.findById(departmentId);
        if (department.getName().equals(ticket.getDepartment().getName())) {
            throw new BusinessException("You cannot transfer to the same department");
        }
        ticket.setDepartment(department);
        updateStatus(ticket, TicketStatus.WAITING);

        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getTicketsByStatus(UUID userId) {

        User operator = getValidOperator(userId);

        return ticketRepository.findByDepartmentAndTicketStatusOrderByCreatedAtAsc(
                operator.getDepartment(),
                TicketStatus.WAITING);

    }

    private Ticket getTicketByBoxAndStatus(Box box, TicketStatus status) {

        return ticketRepository
                .findFirstByBoxAndTicketStatus(box, status)
                .orElseThrow(() -> new ResourceNotFoundException("No ticket with status " + status));
    }

    private User getValidOperator(UUID userId) {

        User user = userService.findById(userId);

        if (user.getBox() == null) {
            throw new BusinessException("User does not have a box assigned");
        }

        return user;
    }

    private void ensureNoActiveTicket(User user) {

        Box box = user.getBox();
        List<TicketStatus> status = List.of(TicketStatus.CALLED, TicketStatus.ATTENDING);
        boolean hasOpenTicket = ticketRepository.existsByBoxAndTicketStatusIn(box, status);

        if (hasOpenTicket) {
            throw new BusinessException(
                    "You must finish the current attendance before calling another ticket");
        }
    }

    private void updateStatus(Ticket ticket, TicketStatus status) {
        ticket.setTicketStatus(status);
        ticket.setLastUpdate(LocalDateTime.now());
    }

}
