package com.hospital.patientregistry.repository;

import com.hospital.patientregistry.model.Diagnosis;
import com.hospital.patientregistry.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    // Search by patient name or code, and optionally by status
    @Query("""
            SELECT d FROM Diagnosis d JOIN d.patient p
            WHERE (:q = '' OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.patientCode) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:status = '' OR d.status = :status)
            ORDER BY d.id DESC
            """)
    List<Diagnosis> search(@Param("q") String q, @Param("status") String status);

    List<Diagnosis> findAllByOrderByIdDesc();

    List<Diagnosis> findTop5ByOrderByIdDesc();

    Optional<Diagnosis> findFirstByPatientOrderByIdDesc(Patient patient);

    List<Diagnosis> findByPatient(Patient patient);

    long countByStatus(String status);
}
