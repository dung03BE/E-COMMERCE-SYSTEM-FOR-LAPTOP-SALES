package com.dung.notification_service.repository;


import com.dung.notification_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface IUserRepository extends JpaRepository<User, Integer> {

    List<User> findByIsActiveTrue();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();
}
