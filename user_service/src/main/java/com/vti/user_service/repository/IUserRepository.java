package com.vti.user_service.repository;

import com.vti.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<Object> findByIsActive(int isActive);
}
