package com.github.maxiamikel.attendancemanagementapi.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketStatus;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query("SELECT COUNT(t) > 0 FROM Ticket t WHERE t.personalId = :personalId AND t.department = :department AND t.ticketStatus IN :activeStatuses")
    boolean existsActiveTicketForUser(String personalId, Department department, List<TicketStatus> activeStatuses);

}
