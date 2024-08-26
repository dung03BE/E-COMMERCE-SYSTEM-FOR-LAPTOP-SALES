package com.dung.notification_service.service;

import com.dung.notification_service.entity.User;
import com.dung.notification_service.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class NotificationDispatcherService {

    private final IUserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private static final int BATCH_SIZE = 1000;
    @Transactional(readOnly = true)
    public void dispatchBlackFridayNotifications()
    {
        List<User> allUsers = userRepository.findByIsActiveTrue();

        for (int i = 0; i < allUsers.size(); i += BATCH_SIZE) {
            List<User> batch = allUsers.subList(i, Math.min(i + BATCH_SIZE, allUsers.size()));
            rabbitTemplate.convertAndSend("notifications-exchange", "black-friday", batch);
        }
    }
}
