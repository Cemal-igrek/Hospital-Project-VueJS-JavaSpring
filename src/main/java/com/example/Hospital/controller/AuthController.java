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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        UserDto userDto = authService.login(loginRequest, response);
        return ResponseEntity.ok(userDto);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto userDto = authService.getCurrentUser();
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/refresh")
    public ResponseEntity<UserDto> refreshToken(@RequestBody RefreshTokenRequestDto request, HttpServletResponse response) {
        UserDto userDto = authService.refreshToken(request, response);
        return ResponseEntity.ok(userDto);
    }
}