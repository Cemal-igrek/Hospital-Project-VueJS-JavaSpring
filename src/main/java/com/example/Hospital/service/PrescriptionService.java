package com.example.Hospital.service;

import com.example.Hospital.dto.CreatePrescriptionRequestDto;
import com.example.Hospital.dto.PrescriptionDto;

import java.util.List;

public interface PrescriptionService {
    PrescriptionDto createPrescription(CreatePrescriptionRequestDto requestDto);
    PrescriptionDto getPrescriptionById(Long id);
    List<PrescriptionDto> getPrescriptionsByAppointmentId(Long appointmentId) throws Exception;
    PrescriptionDto updatePrescription(Long id, CreatePrescriptionRequestDto requestDto);
    void deletePrescription(Long id);
    List<PrescriptionDto> getPrescription();
}
