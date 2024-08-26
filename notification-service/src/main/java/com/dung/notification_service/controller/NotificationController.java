package com.dung.notification_service.controller;

import com.dung.notification_service.service.NotificationDispatcherService;
import com.dung.notification_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")

public class NotificationController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private  UserService userService;
    @Autowired
    private NotificationDispatcherService dispatcherService;



    @GetMapping("/progress")
    public Map<String, Object> getProgress() {
        long totalUsers = userService.getTotalActiveUsers();
        long notifiedUsers = redisTemplate.opsForSet().size("notified_users");

        Map<String, Object> progress = new HashMap<>();
        progress.put("totalUsers", totalUsers);
        progress.put("notifiedUsers", notifiedUsers);
        progress.put("percentage", (double) notifiedUsers / totalUsers * 100);

        return progress;
    }
    @PostMapping("/dispatch")
    public ResponseEntity<String> dispatchNotifications() {
        dispatcherService.dispatchBlackFridayNotifications();
        return ResponseEntity.ok("Notification dispatch initiated");
    }
}
