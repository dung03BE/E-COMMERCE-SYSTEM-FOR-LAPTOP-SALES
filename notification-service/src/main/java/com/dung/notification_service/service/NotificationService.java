package com.dung.notification_service.service;

import com.dung.notification_service.entity.User;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @RabbitListener(queues = "notifications-queue")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void sendNotifications(List<User> users) throws MessagingException, IOException, WriterException {
        for (User user : users) {
            sendNotification(user);
            updateNotificationStatus(user.getId());
        }
    }

    private void sendNotification(User user) throws MessagingException, IOException, WriterException {
        String qrCodeData = "https://yourdomain.com/blackfriday?userId=" +user.getId();
        String emailBody = "<h1>Black Friday Sale!</h1>" +
                "<p>Dear " + user.getFullName() + ",</p>" +
                "<p>Don't miss our amazing Black Friday deals!</p>" +
                "<p>Scan the attached QR code for exclusive offers.</p>";
        emailService.sendEmailWithQRCode(user.getEmail(), "Black Friday Sale!", emailBody, qrCodeData);
    }

    private void updateNotificationStatus(int userId) {
        redisTemplate.opsForSet().add("notified_users", String.valueOf(userId));
    }
}
