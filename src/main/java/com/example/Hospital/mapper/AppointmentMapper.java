package com.example.Hospital.mapper;

import com.example.Hospital.dto.AppointmentDto;
import com.example.Hospital.entity.Appointment;

public class AppointmentMapper {
    public static AppointmentDto toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        return new AppointmentDto(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getDiagnosis(),
                DoctorMapper.toDto(appointment.getDoctor()),
                PatientMapper.toDto(appointment.getPatient())
        );
    }

}
