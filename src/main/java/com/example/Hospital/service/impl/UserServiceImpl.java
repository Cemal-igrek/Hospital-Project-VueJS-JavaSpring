package com.example.Hospital.service.impl;

import com.example.Hospital.dto.CreateUserRequestDto;
import com.example.Hospital.dto.UpdateUserRequestDto;
import com.example.Hospital.dto.UserDto;
import com.example.Hospital.entity.User;
import com.example.Hospital.mapper.UserMapper;
import com.example.Hospital.repository.UserRepository;
import com.example.Hospital.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequestDto requestDto) {

        User user = new User();
        user.setUsername(requestDto.username());
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setEmail(requestDto.email());
        user.setRole(requestDto.role());
        user.setActive(true);

        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id);
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UpdateUserRequestDto requestDto) {
        User existingUser = userRepository.findById(id);

        existingUser.setEmail(requestDto.email());
        existingUser.setRole(requestDto.role());
        existingUser.setActive(requestDto.active());

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id.intValue()); // check this line after ends.
    }
}
