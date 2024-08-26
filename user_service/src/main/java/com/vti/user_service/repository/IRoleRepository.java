package com.vti.user_service.repository;

import com.vti.user_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role,Integer> {

}
