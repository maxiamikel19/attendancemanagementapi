package com.github.maxiamikel.attendancemanagementapi.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.maxiamikel.attendancemanagementapi.entity.Box;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketStatus;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query("""
            SELECT COUNT(t) > 0 FROM Ticket t
            WHERE t.personalId = :personalId
            AND t.department = :department
            AND t.ticketStatus IN :activeStatuses
            """)
    boolean existsActiveTicketForUser(String personalId, Department department, List<TicketStatus> activeStatuses);

    @Query(value = """
                SELECT * FROM tickets t
                WHERE t.department_id = :departmentId
                AND t.ticket_status = :status
                ORDER BY
                    CASE
                        WHEN t.ticket_priority = 'PRIORITY' THEN 0
                        WHEN t.ticket_priority = 'NORMAL' THEN 1
                        ELSE 2
                    END,
                    t.created_at ASC
                FOR UPDATE SKIP LOCKED
                LIMIT 1
            """, nativeQuery = true)
    Optional<Ticket> findNextTicketForUpdateSkipLocked(
            UUID departmentId,
            String status);

    boolean existsByBoxAndTicketStatusIn(Box box, List<TicketStatus> status);
}
