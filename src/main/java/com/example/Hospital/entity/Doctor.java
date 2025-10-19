package com.example.Hospital.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "doctors")
@Data
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 'adSoyad' -> 'fullName'
    private String fullName;

    // 'uzmanlikAlani' -> 'specialty'
    private String specialty;

    // 'telefon' -> 'phone'
    private String phone;

    // 'user' zaten İngilizce, bu doğru
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}