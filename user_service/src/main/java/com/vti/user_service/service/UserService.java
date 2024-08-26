package com.vti.user_service.service;


import com.vti.user_service.entity.User;
import com.vti.user_service.exception.DataNotFoundException;
import com.vti.user_service.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(int id) throws DataNotFoundException {
        User existingUser = userRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Dont find User with id:"+id)
        );
        userRepository.deleteById(id);
    }

    @Override
    public User getUser(int id) throws DataNotFoundException {
        User existingUser = userRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Dont find User with id:"+id)
        );
        return existingUser;
    }

    @Override
    public User getUserActive(int isActive) throws DataNotFoundException {
        User existingUser = (User) userRepository.findByIsActive(isActive).orElseThrow(
                ()->new DataNotFoundException("Dont find User with User:"+isActive)
        );
        return existingUser;
    }


}
