package com.example.Hospital.service;

import com.example.Hospital.dto.ChangePasswordDto;
import com.example.Hospital.dto.LoginRequestDto;
import com.example.Hospital.dto.RefreshTokenRequestDto;
import com.example.Hospital.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserDto login(LoginRequestDto loginRequest, HttpServletResponse response);
    void logout(HttpServletResponse response);

    // EKLENECEK 2: Get Current User
    UserDto getCurrentUser();

    // EKLENECEK 3: Change Password
    void changePassword(ChangePasswordDto request);

    // EKLENECEK 4: Refresh Token (Opsiyonel)
    UserDto refreshToken(RefreshTokenRequestDto request, HttpServletResponse response);
}
