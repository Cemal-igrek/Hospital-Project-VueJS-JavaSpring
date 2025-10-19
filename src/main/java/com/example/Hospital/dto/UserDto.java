package com.example.Hospital.dto;

import com.example.Hospital.entity.Role;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String username,
        String email,
        Role role,
        boolean active,
        LocalDateTime creationDate
) {}