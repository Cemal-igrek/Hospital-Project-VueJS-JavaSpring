package com.example.Hospital.dto;

import java.time.LocalDate;

public record PatientDto(
        Long id,
        String fullName,
        String nationalId,
        LocalDate dateOfBirth,
        String phone
) {}
