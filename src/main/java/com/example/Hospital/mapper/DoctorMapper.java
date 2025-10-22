package com.example.Hospital.mapper;

import com.example.Hospital.dto.DoctorDto;
import com.example.Hospital.entity.Doctor;

public class DoctorMapper {
    public static DoctorDto toDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        return new DoctorDto(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getSpecialty(),
                doctor.getPhone(),
                doctor.getUser() != null ? doctor.getUser().getId() : null
        );
    }

    public static Doctor toEntity(DoctorDto doctorDto) {
        if (doctorDto == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(doctorDto.id());
        doctor.setFullName(doctorDto.fullName());
        doctor.setSpecialty(doctorDto.specialty());
        doctor.setPhone(doctorDto.phone());

        return doctor;
    }
}
