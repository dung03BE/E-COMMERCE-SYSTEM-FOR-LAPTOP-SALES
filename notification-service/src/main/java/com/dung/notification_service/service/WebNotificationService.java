package com.dung.notification_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebNotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String userId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }
}
