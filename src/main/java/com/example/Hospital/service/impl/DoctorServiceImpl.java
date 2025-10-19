package com.example.Hospital.service.impl;

import com.example.Hospital.dto.DoctorDto;
import com.example.Hospital.entity.Doctor;
import com.example.Hospital.entity.User;
import com.example.Hospital.mapper.DoctorMapper;
import com.example.Hospital.repository.DoctorRepository;
import com.example.Hospital.repository.UserRepository;
import com.example.Hospital.service.DoctorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        // Doktorla ilişkilendirilecek kullanıcıyı bul
        User user = userRepository.findById(doctorDto.userId());

        Doctor doctor = DoctorMapper.toEntity(doctorDto);
        doctor.setUser(user);

        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorMapper.toDto(savedDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        return DoctorMapper.toDto(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(DoctorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorDto updateDoctor(Long id, DoctorDto doctorDto) {
        Doctor existingDoctor = doctorRepository.findById(id).orElse(null);

        // Gelen DTO'daki bilgilerle mevcut doktoru güncelle
        existingDoctor.setFullName(doctorDto.fullName());
        existingDoctor.setSpecialty(doctorDto.specialty());
        existingDoctor.setPhone(doctorDto.phone());

        // Gerekirse User'ı da güncelle
        if (doctorDto.userId() != null && !doctorDto.userId().equals(existingDoctor.getUser().getId())) {
            User user = userRepository.findById(doctorDto.userId());
            existingDoctor.setUser(user);
        }

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        return DoctorMapper.toDto(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        if(doctorRepository.findById(id).isPresent()) {
            doctorRepository.deleteById(id);
        }
    }
}
