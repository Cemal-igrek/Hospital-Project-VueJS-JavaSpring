package com.example.Hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 'doktor' -> 'doctor'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // 'hasta' -> 'patient'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // 'tarih' -> 'appointmentDate' ('date' çok genel, bu daha spesifik)
    private LocalDateTime appointmentDate;

    // 'tani' -> 'diagnosis'
    @Column(columnDefinition = "TEXT")
    private String diagnosis;
}