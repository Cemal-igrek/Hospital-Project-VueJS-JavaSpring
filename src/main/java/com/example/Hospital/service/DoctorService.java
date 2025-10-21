package com.example.Hospital.service;

import com.example.Hospital.dto.CreateDoctorRequestDto;
import com.example.Hospital.dto.DoctorDto;

import java.util.List;

public interface DoctorService {
    List<DoctorDto> getAllDoctors();
    DoctorDto getDoctorById(Long id);
    DoctorDto createDoctor(CreateDoctorRequestDto createDoctorRequestDto);
    DoctorDto updateDoctor(Long id, CreateDoctorRequestDto createDoctorRequestDto);
    void deleteDoctor(Long id);
}