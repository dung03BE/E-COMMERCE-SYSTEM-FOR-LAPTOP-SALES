package com.vti.user_service.controller;

import java.util.List;

import com.vti.user_service.dto.UserDto;
import com.vti.user_service.entity.Role;
import com.vti.user_service.exception.DataNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.bytebuddy.description.method.MethodDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vti.user_service.entity.User;
import com.vti.user_service.service.IUserService;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping
    public List<UserDto> getAllUsers()
    {
        List<User> userList = userService.getAllUsers();
        return modelMapper.map(userList,new TypeToken<List<UserDto>>(){}.getType());
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUserById(int id) throws DataNotFoundException {
        userService.deleteUser(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    @GetMapping("{id}")
    public UserDto getUser(int id) throws DataNotFoundException {
        User user = userService.getUser(id);
        return modelMapper.map(user,UserDto.class);
    }
    @GetMapping("{isActive}")
    public UserDto getUserActive(int isActive ) throws DataNotFoundException {
        User user = userService.getUserActive(isActive);
        return modelMapper.map(user,UserDto.class);
    }
}
