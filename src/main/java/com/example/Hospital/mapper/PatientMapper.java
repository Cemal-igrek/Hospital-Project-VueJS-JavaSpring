package com.example.Hospital.mapper;

import com.example.Hospital.dto.PatientDto;
import com.example.Hospital.entity.Patient;

public class PatientMapper {
    public static PatientDto toDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        return new PatientDto(
                patient.getId(),
                patient.getFullName(),
                patient.getNationalId(),
                patient.getDateOfBirth(),
                patient.getPhone()
        );
    }

    public static Patient toEntity(PatientDto patientDto) {
        if (patientDto == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setId(patientDto.id());
        patient.setFullName(patientDto.fullName());
        patient.setNationalId(patientDto.nationalId());
        patient.setDateOfBirth(patientDto.dateOfBirth());
        patient.setPhone(patientDto.phone());
        return patient;
    }
}
