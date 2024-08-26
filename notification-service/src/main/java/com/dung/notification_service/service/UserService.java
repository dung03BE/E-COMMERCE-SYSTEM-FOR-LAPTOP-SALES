package com.dung.notification_service.service;

import com.dung.notification_service.entity.User;
import com.dung.notification_service.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;


    public long getTotalActiveUsers() {
       return  userRepository.countActiveUsers();
    }
}
