package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.dto.request.TicketTransferRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Box;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketPriority;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketStatus;
import com.github.maxiamikel.attendancemanagementapi.exceptions.BusinessException;
import com.github.maxiamikel.attendancemanagementapi.repository.TicketRepository;
import com.github.maxiamikel.attendancemanagementapi.services.AttendanceService;
import com.github.maxiamikel.attendancemanagementapi.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final TicketRepository ticketRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Ticket callNextTicket(UUID userId) {

        User operator = userService.fingById(userId);

        validateOperator(operator);
        ensureNoActiveTicket(operator);

        UUID departmentId = operator.getDepartment().getId();

        Ticket ticket = ticketRepository
                .callNextTicketAtomic(
                        departmentId,
                        TicketStatus.WAITING.name(),
                        operator.getBox().getId())
                .orElseThrow(() -> new BusinessException("No tickets available"));

        log.info("Ticket {} taken by user {}", ticket.getPassCode(), operator.getName());

        return ticket;
    }

    @Override
    @Transactional
    public Ticket callNextTicketByPriority(TicketPriority priority, UUID userId) {

        User operator = userService.fingById(userId);

        validateOperator(operator);
        ensureNoActiveTicket(operator);

        Department department = operator.getDepartment();

        return ticketRepository
                .callNextTicketByPriority(
                        department.getId(),
                        priority.name(),
                        operator.getBox().getId())
                .orElseThrow(() -> new BusinessException("No tickets available"));
    }

    @Override
    public Ticket recallTicket(UUID userId) {
        return null;
    }

    @Override
    @Transactional
    public Ticket startTicket(UUID userId) {

        User operator = userService.fingById(userId);

        validateOperator(operator);

        Box box = operator.getBox();

        Ticket ticket = getTicketByBoxAndStatus(box, TicketStatus.CALLED);

        updateStatus(ticket, TicketStatus.ATTENDING);

        log.info("Ticket {} started attendance at box {}", ticket.getPassCode(),
                box.getBoxNumber());

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket completeTicket(UUID userId) {
        return null;
    }

    @Override
    public Ticket getCurrentTicketTicket() {
        return null;
    }

    @Override
    public Ticket transferTicket(TicketTransferRequest request) {
        return null;
    }

    @Override
    public Ticket cancelTicket(UUID userId) {

        return null;
    }

    @Override
    public List<Ticket> getAllWaitingAttendencesForDepartment() {
        return null;
    }

    private Ticket getTicketByBoxAndStatus(Box box, TicketStatus status) {

        return ticketRepository
                .findFirstByBoxAndTicketStatus(box, status)
                .orElseThrow(() -> new BusinessException("No ticket with status " + status));
    }

    private User validateOperator(User user) {

        if (user == null) {
            throw new BusinessException("User cannot accomplish this action");
        }

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
