package com.example.Hospital.controller;

import com.example.Hospital.dto.AppointmentDto;
import com.example.Hospital.dto.CreateAppointmentRequestDto;
import com.example.Hospital.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Muayeneleri listeleme ve görme
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'DOCTOR')")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'DOCTOR')")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    // Mantıksal ekleme: Doktora göre muayene listeleme
    @GetMapping("/by-doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'DOCTOR')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId));
    }

    // Mantıksal ekleme: Hastaya göre muayene listeleme
    @GetMapping("/by-patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'DOCTOR')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
    }

    // Muayene/Randevu oluşturma (SEKRETER, DOKTOR) ve güncelleme
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'DOCTOR')")
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody CreateAppointmentRequestDto requestDto) {
        AppointmentDto newAppointment = appointmentService.createAppointment(requestDto);
        return new ResponseEntity<>(newAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'DOCTOR')")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long id, @RequestBody CreateAppointmentRequestDto requestDto) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointment(id, requestDto);
        return ResponseEntity.ok(updatedAppointment);
    }

    // Muayene silme (yıkıcı işlem) sadece ADMIN'e aittir
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
