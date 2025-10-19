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

    // 'adSoyad' -> 'fullName'
    private String fullName;

    // 'tcKimlikNo' -> 'nationalId' (Ulusal Kimlik No)
    @Column(unique = true, nullable = false)
    private String nationalId;

    // 'dogumTarihi' -> 'dateOfBirth'
    private LocalDate dateOfBirth;

    // 'telefon' -> 'phone'
    private String phone;
}