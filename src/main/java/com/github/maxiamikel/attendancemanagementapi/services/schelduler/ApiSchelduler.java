package com.github.maxiamikel.attendancemanagementapi.services.schelduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.maxiamikel.attendancemanagementapi.entity.Notification;
import com.github.maxiamikel.attendancemanagementapi.enums.NotificationStatus;
import com.github.maxiamikel.attendancemanagementapi.repository.NotificationRepository;
import com.github.maxiamikel.attendancemanagementapi.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiSchelduler {

    private int ATTEMPS_LIMIT = 3;

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void processPendingNotifications() {

        List<Notification> notifications = notificationRepository.findByStatus(NotificationStatus.PENDING);

        if (!notifications.isEmpty()) {
            for (Notification notification : notifications) {
                try {
                    emailService.sendActivationNotification(notification.getReceiver());

                    notification.setStatus(NotificationStatus.SENT);
                    notification.setAttempts(1);

                    log.info("Email sent to {}", notification.getReceiver());
                } catch (Exception e) {
                    notification.setAttempts(notification.getAttempts() + 1);
                    if (notification.getAttempts() >= ATTEMPS_LIMIT) {
                        notification.setStatus(NotificationStatus.FAILED);
                    }
                }

                notificationRepository.save(notification);
            }
        }

    }
}
