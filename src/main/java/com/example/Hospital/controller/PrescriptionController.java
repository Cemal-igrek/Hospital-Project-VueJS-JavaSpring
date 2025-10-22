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
    @GetMapping
    public ResponseEntity<List<PrescriptionDto>> getPrescription()
    {
        return ResponseEntity.ok(this.prescriptionService.getPrescription());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@PathVariable Long id) {
        PrescriptionDto prescription = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/by-appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByAppointment(@PathVariable Long appointmentId) {
        try {
            return ResponseEntity.ok(prescriptionService.getPrescriptionsByAppointmentId(appointmentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionDto> createPrescription(@RequestBody CreatePrescriptionRequestDto requestDto) {
        PrescriptionDto newPrescription = prescriptionService.createPrescription(requestDto);
        return new ResponseEntity<>(newPrescription, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionDto> updatePrescription(@PathVariable Long id, @RequestBody CreatePrescriptionRequestDto requestDto) {
        PrescriptionDto updatedPrescription = prescriptionService.updatePrescription(id, requestDto);
        return ResponseEntity.ok(updatedPrescription);
    }

    // Reçete silme
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
