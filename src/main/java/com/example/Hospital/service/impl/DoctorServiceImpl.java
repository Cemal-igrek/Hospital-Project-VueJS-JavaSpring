package com.example.Hospital.service.impl;

import com.example.Hospital.dto.CreateDoctorRequestDto;
import com.example.Hospital.dto.DoctorDto;
import com.example.Hospital.entity.Doctor;
import com.example.Hospital.entity.User;
import com.example.Hospital.mapper.DoctorMapper;
import com.example.Hospital.repository.DoctorRepository;
import com.example.Hospital.repository.UserRepository;
import com.example.Hospital.service.DoctorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(DoctorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        return DoctorMapper.toDto(doctor);
    }

    @Override
    @Transactional
    public DoctorDto createDoctor(CreateDoctorRequestDto requestDto) {
        User user = userRepository.findById(requestDto.userId());

        Doctor doctor = new Doctor();
        doctor.setFullName(requestDto.fullName());
        doctor.setSpecialty(requestDto.specialty());
        doctor.setPhone(requestDto.phone());
        doctor.setUser(user);

        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorMapper.toDto(savedDoctor);
    }

    @Override
    @Transactional
    public DoctorDto updateDoctor(Long id, CreateDoctorRequestDto requestDto) {

        Doctor existingDoctor = doctorRepository.findById(id).orElse(null);

        User user = userRepository.findById(requestDto.userId());

        existingDoctor.setFullName(requestDto.fullName());
        existingDoctor.setSpecialty(requestDto.specialty());
        existingDoctor.setPhone(requestDto.phone());
        existingDoctor.setUser(user);

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        return DoctorMapper.toDto(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
