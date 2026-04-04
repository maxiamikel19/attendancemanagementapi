package com.github.maxiamikel.attendancemanagementapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

}
