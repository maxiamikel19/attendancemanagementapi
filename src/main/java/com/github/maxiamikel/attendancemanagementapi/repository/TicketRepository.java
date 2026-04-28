package com.github.maxiamikel.attendancemanagementapi.repository;

import java.time.LocalDateTime;
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
                UPDATE tickets
                SET
                    ticket_status = 'CALLED',
                    box_id = :boxId,
                    last_update = now()
                WHERE id = (
                    SELECT id FROM tickets t
                    WHERE t.department_id = :departmentId
                    AND t.ticket_status = :status
                    ORDER BY
                        CASE
                            WHEN t.ticket_priority = 'PRIORITY' THEN 0
                            WHEN t.ticket_priority = 'NORMAL' THEN 1
                            ELSE 2
                        END,
                        t.created_at ASC
                    LIMIT 1
                    FOR UPDATE SKIP LOCKED
                )
                RETURNING *
            """, nativeQuery = true)
    Optional<Ticket> callNextTicketAtomic(UUID departmentId, String status, UUID boxId);

    boolean existsByBoxAndTicketStatusIn(Box box, List<TicketStatus> status);

    @Query(value = """
                UPDATE tickets
                SET ticket_status = 'CALLED',
                    box_id = :boxId,
                    last_update = now()
                WHERE id = (
                    SELECT id FROM tickets
                    WHERE department_id = :departmentId
                      AND ticket_status = 'WAITING'
                      AND ticket_priority = :priority
                    ORDER BY created_at ASC
                    LIMIT 1
                    FOR UPDATE SKIP LOCKED
                )
                RETURNING *
            """, nativeQuery = true)
    Optional<Ticket> callNextTicketByPriority(UUID departmentId, String priority, UUID boxId);

    Optional<Ticket> findFirstByBoxAndTicketStatus(Box box, TicketStatus status);

    Optional<Ticket> findFirstByBoxAndTicketStatusIn(Box box, List<TicketStatus> status);

    List<Ticket> findByDepartmentAndTicketStatusOrderByCreatedAtAsc(Department department, TicketStatus waiting);

    // Atendimiento

    List<Ticket> findTop10ByTicketStatusOrderByLastUpdateAsc(TicketStatus status);

    List<Ticket> findTop10ByTicketStatusAndLastUpdateBetweenOrderByLastUpdateAsc(
            TicketStatus status,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay);

    // List<Ticket> findTop5ByTicketStatusInOrderByLastUpdateDesc(List<TicketStatus>
    // status);
    // List<Ticket>
    // findTop5ByTicketStatusAndCreatedAtInOrderByLastUpdateDesc(List<TicketStatus>
    // status);

    List<Ticket> findTop5ByTicketStatusInAndLastUpdateBetweenOrderByLastUpdateDesc(
            List<TicketStatus> status,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay);

    Optional<Ticket> findTop1ByTicketStatusOrderByLastUpdateDesc(TicketStatus called);
}
