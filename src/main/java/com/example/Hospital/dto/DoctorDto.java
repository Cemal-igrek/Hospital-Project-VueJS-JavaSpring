package com.example.Hospital.dto;

public record DoctorDto(
        Long id,
        String fullName,
        String specialty,
        String phone,
        Long userId
) {}