package com.hospital.patientregistry.controller;

import com.hospital.patientregistry.dto.PatientRecord;
import com.hospital.patientregistry.model.Diagnosis;
import com.hospital.patientregistry.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientApiController {

    private final PatientService service;

    public PatientApiController(PatientService service) {
        this.service = service;
    }

    @GetMapping
    public List<Diagnosis> getAll() {
        return service.findAllRecords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientRecord> getOne(@PathVariable Long id) {
        return ResponseEntity.of(service.findRecord(id));
    }

    @PostMapping
    public ResponseEntity<Diagnosis> create(@Valid @RequestBody PatientRecord record) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Diagnosis> update(@PathVariable Long id, @Valid @RequestBody PatientRecord record) {
        return ResponseEntity.of(service.update(id, record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
