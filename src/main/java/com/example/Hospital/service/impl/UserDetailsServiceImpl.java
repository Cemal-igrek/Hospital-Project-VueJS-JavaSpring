package com.example.Hospital.service.impl; // Senin paket adın

// Gerekli import'lar
import com.example.Hospital.entity.User;
import com.example.Hospital.entity.Role; // Role enum'unu import ettiğinden emin ol
import com.example.Hospital.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections; // Veya java.util.List

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security AuthenticationManager tarafından çağrılır.
     * Kullanıcı adını alır, veritabanından kullanıcıyı bulur ve
     * Spring Security'nin anlayacağı UserDetails nesnesini döndürür.
     * @param username Giriş formundan gelen kullanıcı adı
     * @return UserDetails Spring Security'nin kullanacağı kullanıcı bilgisi
     * @throws UsernameNotFoundException Kullanıcı bulunamazsa fırlatılır
     */
    @Override
    @Transactional(readOnly = true) // Sadece okuma işlemi
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Veritabanından kendi User entity'mizi bul
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. Kendi Role enum'umuzu Spring Security'nin GrantedAuthority formatına çevir
        // EN ÖNEMLİ SATIR: "ROLE_" önekini eklediğinden emin ol!
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        // 3. Spring Security'nin kendi UserDetails nesnesini oluştur ve döndür
        // Son parametre olarak yetkiyi bir liste içinde ver (Collections.singletonList veya List.of)
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // Kullanıcı adı
                user.getPassword(), // Veritabanındaki HASH'lenmiş şifre
                user.isActive(),    // Hesap aktif mi? (Enabled)
                true,               // Hesap süresi dolmamış mı? (AccountNonExpired)
                true,               // Şifre süresi dolmamış mı? (CredentialsNonExpired)
                true,               // Hesap kilitli değil mi? (AccountNonLocked)
                Collections.singletonList(authority) // Yetkiler (Roller)
        );
    }
}