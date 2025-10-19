package com.example.Hospital.dto;

public record CreateDoctorRequestDto(
        String fullName,
        String specialty,
        String phone,
        Long userId
) {
}
