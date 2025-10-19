package com.example.Hospital.dto;

public record PrescriptionDto(
        Long id,
        Long appointmentId,
        String medicationName,
        String dose,
        String instructions
) {
}
