package com.hospital.patientregistry.service;

import com.hospital.patientregistry.model.Patient;
import com.hospital.patientregistry.repository.PatientRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
 * Service layer: all business logic and transaction boundaries live here.
 * Class-level @Transactional applies to every method; read-only methods
 * are marked readOnly = true for better performance.
 */
@Service
@Transactional
public class PatientService {

    private final PatientRepository repo;

    public PatientService(PatientRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Patient> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Patient> search(String q, String status) {
        return repo.search(q.trim(), status, Sort.by("registeredAt").descending());
    }

    @Transactional(readOnly = true)
    public List<Patient> recent() {
        return repo.findTop5ByOrderByRegisteredAtDesc();
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return repo.count();
    }

    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return repo.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public long countRegisteredToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        return repo.countByRegisteredAtBetween(start, start.plusDays(1));
    }

    public Patient save(Patient patient) {
        return repo.save(patient);
    }

    // Read and write happen in one transaction, so a failure rolls back both.
    public Optional<Patient> update(Long id, Patient form) {
        return repo.findById(id).map(p -> {
            p.setFullName(form.getFullName());
            p.setDateOfBirth(form.getDateOfBirth());
            p.setSex(form.getSex());
            p.setPhone(form.getPhone());
            p.setWard(form.getWard());
            p.setBed(form.getBed());
            p.setStatus(form.getStatus());
            return repo.save(p);
        });
    }

    public boolean delete(Long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }
}