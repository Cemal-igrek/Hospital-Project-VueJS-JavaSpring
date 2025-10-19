package com.example.Hospital.dto;

import com.example.Hospital.entity.Role;

public record CreateUserRequestDto(
        String username,
        String password,
        String email,
        Role role
) {}
