package com.example.Hospital.service.impl;

import com.example.Hospital.entity.User;
import com.example.Hospital.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true) // Veritabanı okuma işlemi
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kendi User entity'mizi username ile buluyoruz
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Kendi Role enum'umuzu Spring Security'nin anladığı formata (GrantedAuthority) çeviriyoruz
        // PDF'teki rol adları (ADMIN, DOCTOR) başına "ROLE_" eklenerek kullanılır
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        // Spring Security'nin kendi User nesnesini oluşturup döndürüyoruz
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(), // 'aktif' alanını da kontrol ediyoruz
                true,
                true,
                true,
                List.of(authority) // Rolleri
        );
    }
}
