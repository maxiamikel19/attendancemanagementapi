package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.entity.Notification;
import com.github.maxiamikel.attendancemanagementapi.enums.NotificationStatus;
import com.github.maxiamikel.attendancemanagementapi.repository.NotificationRepository;
import com.github.maxiamikel.attendancemanagementapi.services.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void createActivationAccountNotification(String email) {
        Notification notification = Notification
                .builder()
                .receiver(email)
                .status(NotificationStatus.PENDING)
                .attempts(0)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

}
