package com.github.maxiamikel.attendancemanagementapi.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.services.EmailService;
import com.github.maxiamikel.attendancemanagementapi.services.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    private final JavaMailSender mailSender;
    private final UserService userService;

    @Override
    public void sendActivationNotification(String email) {
        User user = userService.findByEmail(email);
        String subject = "Account Activation Notification";

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(generateMessage(user), true);
            mailSender.send(message);

            log.info("Sent activation notification to: {}", user.getEmail());
        } catch (MailException | MessagingException e) {
            log.error("Failed sendding notification to {}", user.getEmail());
        }
    }

    private String generateMessage(User user) {
        String apiLink = apiBaseUrl + "/v1/users/activate-account?id=" + user.getId();

        String message = "<div style='background:#f1f1f1;padding:10px 20px;border-radius:10px;font-family:sans-serif'>"
                +
                "<h2>Activation Account Notification</h2>" +
                "<p>Hi <span style='font-weight:bold;text-transform:capitalize;'>" + user.getName()
                + "</span>, please confirm your email to activate your account.</p>" +
                "<p><a href='" + apiLink
                + "' style='display:inline-block;padding:10px 16px;background:#6366f1;color:#f1f1f1;border-radius:6px;text-decoration:none;'> Activate Account</a> </p>"
                +
                "<p>Or copy this link: " + apiLink + "</p>" +
                "<h4>Thank you!</h4>" +
                "<h4>Att: <small style='color:#6366f1'>Amikel Maxi Celestin</small></h4>" +
                "</div>";
        return message;
    }

}
