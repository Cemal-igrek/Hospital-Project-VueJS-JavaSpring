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

    private String fullName;

    private String specialty;

    private String phone;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // cascade eklendi
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true) // unique=true eklemek iyi bir pratik
    private User user;
}