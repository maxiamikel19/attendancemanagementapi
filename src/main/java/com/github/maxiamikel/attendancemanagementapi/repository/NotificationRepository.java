package com.github.maxiamikel.attendancemanagementapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maxiamikel.attendancemanagementapi.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

}
