package com.hospital.patientregistry.repository;

import com.hospital.patientregistry.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    long countByRegisteredAtBetween(LocalDateTime start, LocalDateTime end);
}
