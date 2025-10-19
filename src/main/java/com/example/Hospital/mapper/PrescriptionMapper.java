package com.example.Hospital.mapper;

import com.example.Hospital.dto.PrescriptionDto;
import com.example.Hospital.entity.Prescription;

public class PrescriptionMapper {
    public static PrescriptionDto toDto(Prescription prescription) {
        if (prescription == null) {
            return null;
        }
        return new PrescriptionDto(
                prescription.getId(),
                prescription.getAppointment().getId(),
                prescription.getMedicationName(),
                prescription.getDose(),
                prescription.getInstructions()
        );
    }
}
