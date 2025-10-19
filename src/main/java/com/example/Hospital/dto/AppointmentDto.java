package com.example.Hospital.dto;

import java.time.LocalDateTime;

public record AppointmentDto(
        Long id,
        LocalDateTime appointmentDate,
        String diagnosis,
        DoctorDto doctor,
        PatientDto patient

) {

}
