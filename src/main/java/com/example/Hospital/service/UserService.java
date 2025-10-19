package com.example.Hospital.service;

import com.example.Hospital.dto.CreateUserRequestDto;
import com.example.Hospital.dto.UpdateUserRequestDto;
import com.example.Hospital.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserRequestDto requestDto);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id, UpdateUserRequestDto requestDto);
    void deleteUser(Long id);
}
