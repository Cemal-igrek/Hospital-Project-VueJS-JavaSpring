package com.example.Hospital.service;

import com.example.Hospital.dto.AppointmentDto;
import com.example.Hospital.dto.CreateAppointmentRequestDto;

import java.util.List;

public interface AppointmentService {
    AppointmentDto createAppointment(CreateAppointmentRequestDto requestDto);
    AppointmentDto getAppointmentById(Long id);
    List<AppointmentDto> getAllAppointments();
    List<AppointmentDto> getAppointmentsByDoctorId(Long doctorId);
    List<AppointmentDto> getAppointmentsByPatientId(Long patientId);
    AppointmentDto updateAppointment(Long id, CreateAppointmentRequestDto requestDto);
    void deleteAppointment(Long id);
}
