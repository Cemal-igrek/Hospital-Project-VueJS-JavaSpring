package com.example.Hospital.service.impl;

import com.example.Hospital.dto.CreatePrescriptionRequestDto;
import com.example.Hospital.dto.PrescriptionDto;
import com.example.Hospital.entity.Appointment;
import com.example.Hospital.entity.Prescription;
import com.example.Hospital.mapper.PrescriptionMapper;
import com.example.Hospital.repository.AppointmentRepository;
import com.example.Hospital.repository.PrescriptionRepository;
import com.example.Hospital.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }


    @Override
    @Transactional
    public PrescriptionDto createPrescription(CreatePrescriptionRequestDto requestDto) {
        Appointment appointment = appointmentRepository.findById(requestDto.appointmentId()).orElse(null);

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setMedicationName(requestDto.medicationName());
        prescription.setDose(requestDto.dose());
        prescription.setInstructions(requestDto.instructions());

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return PrescriptionMapper.toDto(savedPrescription);
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionDto getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id).orElse(null);
        return PrescriptionMapper.toDto(prescription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionDto> getPrescriptionsByAppointmentId(Long appointmentId) throws Exception {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new Exception("Appointment not found with id");
        }

        return prescriptionRepository.findByAppointmentId(appointmentId).stream() // Bu metodu Repository'de oluşturun
                .map(PrescriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrescriptionDto updatePrescription(Long id, CreatePrescriptionRequestDto requestDto) {
        Prescription existingPrescription = prescriptionRepository.findById(id).orElse(null);

        if (!requestDto.appointmentId().equals(existingPrescription.getAppointment().getId())) {
            Appointment appointment = appointmentRepository.findById(requestDto.appointmentId()).orElse(null);
            existingPrescription.setAppointment(appointment);
        }

        existingPrescription.setMedicationName(requestDto.medicationName());
        existingPrescription.setDose(requestDto.dose());
        existingPrescription.setInstructions(requestDto.instructions());

        Prescription updatedPrescription = prescriptionRepository.save(existingPrescription);
        return PrescriptionMapper.toDto(updatedPrescription);
    }

    @Override
    @Transactional
    public void deletePrescription(Long id) {
        if (prescriptionRepository.findById(id).isPresent()) {
            prescriptionRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public List<PrescriptionDto> getPrescription() {
        return prescriptionRepository.findAll().stream().map(PrescriptionMapper::toDto).collect(Collectors.toList());
    }
}


