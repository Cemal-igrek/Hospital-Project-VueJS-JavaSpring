package com.example.Hospital.dto;

public record ChangePasswordDto(
        String oldPassword,
        String newPassword
) {
}
