package com.example.Hospital.mapper;

import com.example.Hospital.dto.UserDto;
import com.example.Hospital.entity.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreationDate()
        );

    }
}
