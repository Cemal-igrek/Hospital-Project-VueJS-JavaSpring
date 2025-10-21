package com.example.Hospital.controller;

import com.example.Hospital.dto.UserDto;
import com.example.Hospital.dto.ChangePasswordDto;
import com.example.Hospital.dto.LoginRequestDto;
import com.example.Hospital.dto.RefreshTokenRequestDto;
import com.example.Hospital.service.AuthService;
import jakarta.servlet.http.HttpServletResponse; // Cookie yönetimi için gerekli
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // AuthService'i enjekte et
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/login [cite: 129]
    // httpOnly cookie set eder ve body'de UserDto döner
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        UserDto userDto = authService.login(loginRequest, response);
        return ResponseEntity.ok(userDto);
    }

    // POST /api/auth/logout [cite: 130]
    // httpOnly cookie'yi temizler
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }

    // GET /api/auth/me [cite: 132]
    // Cookie'den (JWT Filter ile) alınan kullanıcıyı döner
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Sadece giriş yapmış kullanıcılar
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto userDto = authService.getCurrentUser();
        return ResponseEntity.ok(userDto);
    }

    // PUT /api/auth/change-password [cite: 135]
    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()") // Sadece giriş yapmış kullanıcılar
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build(); // Body'siz 200 OK
    }

    // POST /api/auth/refresh (Opsiyonel) [cite: 131, 134]
    // Opsiyonel refresh token'ı alır, yeni bir httpOnly cookie set eder
    @PostMapping("/refresh")
    public ResponseEntity<UserDto> refreshToken(@RequestBody RefreshTokenRequestDto request, HttpServletResponse response) {
        UserDto userDto = authService.refreshToken(request, response);
        return ResponseEntity.ok(userDto);
    }
}