package com.vti.user_service.service;

import com.vti.user_service.entity.User;
import com.vti.user_service.exception.DataNotFoundException;

import java.util.List;

public interface IUserService {

    List<User> getAllUsers();


    void deleteUser(int id) throws DataNotFoundException;

    User getUser(int id) throws DataNotFoundException;

    User getUserActive(int isActive) throws DataNotFoundException;
}
