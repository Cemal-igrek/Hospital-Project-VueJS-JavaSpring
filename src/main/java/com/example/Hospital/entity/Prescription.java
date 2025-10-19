package com.example.Hospital.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "prescriptions")
@Data
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 'muayene' -> 'appointment' (Bu reçete bir muayeneye aittir)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;

    // 'ilacAdi' -> 'medicationName'
    private String medicationName;

    // 'doz' -> 'dose'
    private String dose;

    // 'aciklama' -> 'instructions' (Açıklama/Talimatlar)
    @Column(columnDefinition = "TEXT")
    private String instructions;
}