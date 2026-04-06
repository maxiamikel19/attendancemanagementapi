package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.maxiamikel.attendancemanagementapi.dto.request.TicketRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketStatus;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ActiveTicketAlreadyExistsException;
import com.github.maxiamikel.attendancemanagementapi.repository.TicketRepository;
import com.github.maxiamikel.attendancemanagementapi.services.DepartmentCounterService;
import com.github.maxiamikel.attendancemanagementapi.services.DepartmentService;
import com.github.maxiamikel.attendancemanagementapi.services.TicketService;
import com.github.maxiamikel.attendancemanagementapi.utils.ApiUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final DepartmentService departmentService;
    private final DepartmentCounterService departmentCounterService;

    @Override
    public Ticket generateTicket(TicketRequest request) {

        Department department = departmentService.findByName(request.getDepartment());

        validateNoActiveTicket(request.getPersonalId(), department);

        Ticket ticket = buildTicket(request, department);

        log.info("New ticket {} successfully generated", ticket.getPassCode());

        return ticketRepository.save(ticket);
    }

    private Ticket buildTicket(TicketRequest request, Department department) {

        String passCode = generatePassCode(department);

        return Ticket.builder()
                .createdAt(LocalDateTime.now())
                .department(department)
                .passCode(passCode)
                .personalId(request.getPersonalId())
                .priority(request.getPriority())
                .recallCount(0)
                .ticketStatus(TicketStatus.WAITING)
                .build();
    }

    private String generatePassCode(Department department) {

        String cleanName = ApiUtils.normalizeStringToUpperCase(department.getName());
        String prefix = cleanName.length() >= 2 ? cleanName.substring(0, 2) : cleanName;

        Long nextNumber = departmentCounterService.getNextNumberByDepartment(department);

        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return String.format("%s-%s-%03d", prefix, date, nextNumber);
    }

    private void validateNoActiveTicket(String personalId, Department department) {

        List<TicketStatus> activeStatuses = List.of(
                TicketStatus.WAITING,
                TicketStatus.ATTENDING);

        boolean exists = ticketRepository.existsActiveTicketForUser(
                personalId,
                department,
                activeStatuses);

        if (exists) {
            throw new ActiveTicketAlreadyExistsException(personalId, department.getName());
        }
    }

}
