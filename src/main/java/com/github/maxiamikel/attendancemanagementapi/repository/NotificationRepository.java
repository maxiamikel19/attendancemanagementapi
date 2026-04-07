package com.github.maxiamikel.attendancemanagementapi.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maxiamikel.attendancemanagementapi.entity.Notification;
import com.github.maxiamikel.attendancemanagementapi.enums.NotificationStatus;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByStatus(NotificationStatus pending);

}
