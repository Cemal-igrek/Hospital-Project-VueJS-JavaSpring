package com.example.Hospital.service.impl;

import com.example.Hospital.dto.PatientDto;
import com.example.Hospital.entity.Patient;
import com.example.Hospital.mapper.PatientMapper;
import com.example.Hospital.repository.PatientRepository;
import com.example.Hospital.service.PatientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public PatientDto createPatient(PatientDto patientDto) {
        Patient patient = PatientMapper.toEntity(patientDto);
        Patient savedPatient = patientRepository.save(patient);
        return PatientMapper.toDto(savedPatient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        return PatientMapper.toDto(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(PatientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        Patient existingPatient = patientRepository.findById(id).orElse(null);

        existingPatient.setFullName(patientDto.fullName());
        existingPatient.setNationalId(patientDto.nationalId());
        existingPatient.setDateOfBirth(patientDto.dateOfBirth());
        existingPatient.setPhone(patientDto.phone());

        Patient updatedPatient = patientRepository.save(existingPatient);
        return PatientMapper.toDto(updatedPatient);
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
