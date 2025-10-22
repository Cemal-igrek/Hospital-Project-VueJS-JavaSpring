package com.example.Hospital.service.impl;

import com.example.Hospital.dto.CreateUserRequestDto;
import com.example.Hospital.dto.UpdateUserRequestDto;
import com.example.Hospital.dto.UserDto;
import com.example.Hospital.entity.Doctor;
import com.example.Hospital.entity.Role;
import com.example.Hospital.entity.User;
import com.example.Hospital.mapper.UserMapper;
import com.example.Hospital.repository.DoctorRepository;
import com.example.Hospital.repository.UserRepository;
import com.example.Hospital.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorRepository doctorRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.doctorRepository = doctorRepository;
    }

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequestDto requestDto) {

        User user = new User();
        user.setUsername(requestDto.username());
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setEmail(requestDto.email());
        user.setRole(requestDto.role());
        user.setActive(true);

        // 1. User'ı kaydet
        User savedUser = userRepository.save(user);

        // 2. *** DEĞİŞEN KISIM: Rol DOCTOR ise DTO'dan gelen bilgilerle Doctor oluştur ***
        if (savedUser.getRole() == Role.DOCTOR) {

            // DTO'dan gelen doktor bilgilerini kontrol et (Frontend göndermemiş olabilir)
            if (requestDto.doctorFullName() == null || requestDto.doctorFullName().isBlank()) {
                // Eğer frontend bilgileri göndermediyse (veya validasyon eklenirse) hata fırlatılabilir
                throw new IllegalArgumentException("Doctor details (Full Name) are required when creating a user with DOCTOR role.");
                // VEYA placeholder kullanmaya geri dönebiliriz:
                // doctor.setFullName("Lütfen Güncelleyin");
            }

            Doctor doctor = new Doctor();
            doctor.setUser(savedUser); // User'ı bağla

            doctor.setFullName(requestDto.doctorFullName());
            doctor.setSpecialty(requestDto.doctorSpecialty() != null ? requestDto.doctorSpecialty() : "Belirtilmedi"); // Specialty boş olabilir
            doctor.setPhone(requestDto.doctorPhone() != null ? requestDto.doctorPhone() : ""); // Phone boş olabilir

            doctorRepository.save(doctor);
        }

        return UserMapper.toDto(savedUser);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id);
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UpdateUserRequestDto requestDto) {
        User existingUser = userRepository.findById(id);

        existingUser.setEmail(requestDto.email());
        existingUser.setRole(requestDto.role());
        existingUser.setActive(requestDto.active());

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        // 1. Önce silinecek User'ı bulalım (var mı diye kontrol etmek için)
        User userToDelete = userRepository.findById(id);


        // 2. Eğer kullanıcının rolü DOKTOR ise, ilişkili Doktor profilini ÖNCE SİL
        if (userToDelete.getRole() == Role.DOCTOR) {

            // İlişkili Doktor'u User ID'si ile bul
            Optional<Doctor> doctorOptional = doctorRepository.findByUserId(id);

            if (doctorOptional.isPresent()) {
                Doctor doctorToDelete = doctorOptional.get();
                doctorRepository.delete(doctorToDelete); // Doktoru SİL
            }
        }

        userRepository.delete(userToDelete); // User'ı SİL

    }}

