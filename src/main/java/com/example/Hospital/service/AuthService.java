package com.example.Hospital.service;

import com.example.Hospital.dto.ChangePasswordDto;
import com.example.Hospital.dto.LoginRequestDto;
import com.example.Hospital.dto.RefreshTokenRequestDto;
import com.example.Hospital.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserDto login(LoginRequestDto loginRequest, HttpServletResponse response);
    void logout(HttpServletResponse response);

    UserDto getCurrentUser();

    void changePassword(ChangePasswordDto request);

    UserDto refreshToken(RefreshTokenRequestDto request, HttpServletResponse response);
}
