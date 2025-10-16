package com.example.Hospital.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adSoyad;

    @Column(unique = true, nullable = false)
    private String tcKimlikNo;

    private LocalDate dogumTarihi;
    private String telefon;
}
