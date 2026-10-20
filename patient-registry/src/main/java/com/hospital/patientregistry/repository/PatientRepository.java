package com.hospital.patientregistry.repository;

import com.hospital.patientregistry.model.Patient;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("""
            SELECT p FROM Patient p
            WHERE (:q = '' OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.patientCode) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:status = '' OR p.status = :status)
            """)
    List<Patient> search(@Param("q") String q, @Param("status") String status, Sort sort);

    List<Patient> findTop5ByOrderByRegisteredAtDesc();

    long countByStatus(String status);

    long countByRegisteredAtBetween(LocalDateTime start, LocalDateTime end);
}