package com.example.Hospital.controller;

import com.example.Hospital.dto.CreatePrescriptionRequestDto;
import com.example.Hospital.dto.PrescriptionDto;
import com.example.Hospital.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    // Reçeteleri sadece DOKTOR ve ADMIN görebilir [cite: 87, 90]
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@PathVariable Long id) {
        PrescriptionDto prescription = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }

    // Bir muayeneye ait reçeteleri listeleme
    @GetMapping("/by-appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByAppointment(@PathVariable Long appointmentId) {
        try {
            return ResponseEntity.ok(prescriptionService.getPrescriptionsByAppointmentId(appointmentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Reçete oluşturma (yazma)
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')") // Reçeteyi sadece DOKTOR yazabilir
    public ResponseEntity<PrescriptionDto> createPrescription(@RequestBody CreatePrescriptionRequestDto requestDto) {
        PrescriptionDto newPrescription = prescriptionService.createPrescription(requestDto);
        return new ResponseEntity<>(newPrescription, HttpStatus.CREATED);
    }

    // PDF'te reçete güncelleme (7.6) belirtilmiş [cite: 121]
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionDto> updatePrescription(@PathVariable Long id, @RequestBody CreatePrescriptionRequestDto requestDto) {
        PrescriptionDto updatedPrescription = prescriptionService.updatePrescription(id, requestDto);
        return ResponseEntity.ok(updatedPrescription);
    }

    // Reçete silme
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')") // Doktor kendi yazdığını veya Admin silebilir [cite: 87, 90]
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
