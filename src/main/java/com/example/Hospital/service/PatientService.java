package com.example.Hospital.service;

import com.example.Hospital.dto.PatientDto;

import java.util.List;

public interface PatientService {
    PatientDto createPatient(PatientDto patientDto);
    PatientDto getPatientById(Long id);
    List<PatientDto> getAllPatients();
    PatientDto updatePatient(Long id, PatientDto patientDto);
    void deletePatient(Long id);
}
