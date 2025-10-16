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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment muayene;

    private String ilacAdi;
    private String doz;

    @Column(columnDefinition = "TEXT")
    private String aciklama;
}