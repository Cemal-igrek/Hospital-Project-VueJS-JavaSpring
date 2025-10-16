package com.example.Hospital.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String adSoyad;
    private String uzmanlikAlani;
    private String telefon;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
