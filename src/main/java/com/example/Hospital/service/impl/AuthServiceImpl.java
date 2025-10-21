package com.example.Hospital.service.impl;

import com.example.Hospital.dto.ChangePasswordDto;
import com.example.Hospital.dto.LoginRequestDto;
import com.example.Hospital.dto.RefreshTokenRequestDto;
import com.example.Hospital.dto.UserDto;
import com.example.Hospital.entity.User;
import com.example.Hospital.mapper.UserMapper;
import com.example.Hospital.repository.UserRepository;
import com.example.Hospital.service.AuthService;
import com.example.Hospital.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final long jwtExpirationMs; // Cookie 'maxAge' için
    private final PasswordEncoder passwordEncoder; // YENİ: Şifre değişikliği için

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           JwtService jwtService,
                           @Value("${jwt.expiration-ms}") long jwtExpirationMs,
    PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.jwtExpirationMs = jwtExpirationMs;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto login(LoginRequestDto loginRequest, HttpServletResponse response) {
        // 1. Kimlik doğrulama (Aynı)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        // 2. UserDetails'i al (Aynı)
        var userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Token üret (Aynı)
        String token = jwtService.generateToken(userDetails);

        // 4. *** YENİ: HTTPOnly Cookie Oluştur ***
        // Bu, dökümandaki  önerisidir.
        ResponseCookie cookie = ResponseCookie.from("auth-token", token) // Cookie'nin adı
                .httpOnly(true)       // JavaScript'in erişimini engeller (XSS Koruması)
                .secure(false)        // TODO: Production'da (HTTPS) 'true' olmalı
                .path("/")            // Cookie tüm site için geçerli
                .maxAge(jwtExpirationMs / 1000) // Cookie ömrü (saniye cinsinden)
                .build();

        // 5. Cookie'yi yanıta (response) header olarak ekle
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 6. Token'ı body'de DEĞİL, kullanıcı bilgilerini döndür
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after login"));

        return UserMapper.toDto(user);
    }
    @Override
    public void logout(HttpServletResponse response) {
        // httpOnly cookie'yi temizlemek için maxAge=0 olan bir cookie oluştur
        ResponseCookie cookie = ResponseCookie.from("auth-token", "")
                .httpOnly(true)
                .secure(false) // TODO: Production'da 'true' olmalı
                .path("/")
                .maxAge(0) // Cookie'yi anında geçersiz kıl
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        SecurityContextHolder.clearContext(); // Güvenlik context'ini temizle
    }

    // EKLENECEK 2: Get Current User
    @Override
    public UserDto getCurrentUser() {
        // JWT Filtresi sayesinde SecurityContext'e atadığımız kullanıcıyı al
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        return UserMapper.toDto(user);
    }

    // EKLENECEK 3: Change Password
    @Override
    public void changePassword(ChangePasswordDto request) {
        // Mevcut giriş yapmış kullanıcıyı al
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElse(null);

        // 1. Eski şifreyi kontrol et
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong old password"); // Veya özel bir exception
        }

        // 2. Yeni şifreyi hash'le ve kaydet
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    // EKLENECEK 4: Refresh Token (Opsiyonel)
    @Override
    public UserDto refreshToken(RefreshTokenRequestDto request, HttpServletResponse response) {
        // TODO: Refresh token mantığı burada implemente edilmeli
        // 1. Gelen request.refreshToken() veritabanında/cache'te var mı? Geçerli mi?
        // 2. Eğer geçerliyse, o token'a ait kullanıcıyı bul (örn: User user = ...)
        // 3. O kullanıcı için yeni bir access token (JWT) üret:
        //    String token = jwtService.generateToken(new UserDetailsServiceImpl.CustomUserDetails(user));
        // 4. Yeni token'ı httpOnly cookie olarak set et (logout'taki gibi)
        // 5. O kullanıcıya ait UserDto'yu döndür

        // Şimdilik taslak:
        throw new UnsupportedOperationException("Refresh token functionality not implemented yet.");
    }
}
