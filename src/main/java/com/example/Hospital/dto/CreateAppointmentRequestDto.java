package com.example.Hospital.dto;

import java.time.LocalDateTime;

public record CreateAppointmentRequestDto(Long doctorId,
                                          Long patientId,
                                          LocalDateTime appointmentDate,
                                          String diagnosis) {
}
