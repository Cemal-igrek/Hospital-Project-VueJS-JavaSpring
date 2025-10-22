package com.example.Hospital.service.impl;

import com.example.Hospital.dto.AppointmentDto;
import com.example.Hospital.dto.CreateAppointmentRequestDto;
import com.example.Hospital.entity.Appointment;
import com.example.Hospital.entity.Doctor;
import com.example.Hospital.entity.Patient;
import com.example.Hospital.mapper.AppointmentMapper;
import com.example.Hospital.repository.AppointmentRepository;
import com.example.Hospital.repository.DoctorRepository;
import com.example.Hospital.repository.PatientRepository;
import com.example.Hospital.service.AppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  DoctorRepository doctorRepository,
                                  PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public AppointmentDto createAppointment(CreateAppointmentRequestDto requestDto) {
        Doctor doctor = doctorRepository.findById(requestDto.doctorId()).orElse(null);

        Patient patient = patientRepository.findById(requestDto.patientId()).orElse(null);

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(requestDto.appointmentDate());
        appointment.setDiagnosis(requestDto.diagnosis());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return AppointmentMapper.toDto(savedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        return AppointmentMapper.toDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAppointmentsByDoctorId(Long doctorId) {
            return appointmentRepository.findByDoctorId(doctorId).stream() // Bu metodu Repository'de oluşturmanız gerek
                    .map(AppointmentMapper::toDto)
                    .collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAppointmentsByPatientId(Long patientId) {
            return appointmentRepository.findByPatientId(patientId).stream() // Bu metodu Repository'de oluşturmanız gerek
                    .map(AppointmentMapper::toDto)
                    .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public AppointmentDto updateAppointment(Long id, CreateAppointmentRequestDto requestDto) {
        Appointment existingAppointment = appointmentRepository.findById(id).orElse(null);

        if (!requestDto.doctorId().equals(existingAppointment.getDoctor().getId())) {
            Doctor doctor = doctorRepository.findById(requestDto.doctorId()).orElse(null);
            existingAppointment.setDoctor(doctor);
        }

        if (!requestDto.patientId().equals(existingAppointment.getPatient().getId())) {
            Patient patient = patientRepository.findById(requestDto.patientId()).orElse(null);
            existingAppointment.setPatient(patient);
        }

        existingAppointment.setAppointmentDate(requestDto.appointmentDate());
        existingAppointment.setDiagnosis(requestDto.diagnosis());

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return AppointmentMapper.toDto(updatedAppointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        }
    }
}
