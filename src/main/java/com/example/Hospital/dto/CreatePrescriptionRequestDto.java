package com.example.Hospital.dto;

public record CreatePrescriptionRequestDto(
        Long appointmentId,
        String medicationName,
        String dose,
        String instructions
) {
}
