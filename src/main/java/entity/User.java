package entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false) //this have to be unique because its nickname XD
    private String kullaniciAdi;
    @Column(nullable = false)
    private String sifre;

    @Enumerated(EnumType.STRING) // for enum to string
    @Column(nullable = false)
    private Role rol;

    private boolean aktif = true;
    private LocalDateTime olusturmaTarihi;

    @PrePersist
    public void prePersist() {
        olusturmaTarihi = LocalDateTime.now();
    }
}
