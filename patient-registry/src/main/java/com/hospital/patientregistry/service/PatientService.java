package com.hospital.patientregistry.service;

import com.hospital.patientregistry.dto.PatientRecord;
import com.hospital.patientregistry.model.Diagnosis;
import com.hospital.patientregistry.model.Patient;
import com.hospital.patientregistry.repository.DiagnosisRepository;
import com.hospital.patientregistry.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
 * Service layer: all business logic and transaction boundaries.
 * Registering or updating a record saves the patient and the diagnosis
 * in one transaction, so both succeed or both roll back.
 */
@Service
@Transactional
public class PatientService {

    private final PatientRepository patients;
    private final DiagnosisRepository diagnoses;

    public PatientService(PatientRepository patients, DiagnosisRepository diagnoses) {
        this.patients = patients;
        this.diagnoses = diagnoses;
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> findAllRecords() {
        return diagnoses.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> search(String q, String status) {
        return diagnoses.search(q.trim(), status);
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> recent() {
        return diagnoses.findTop5ByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public Optional<PatientRecord> findRecord(Long patientId) {
        return patients.findById(patientId).map(p -> {
            PatientRecord r = new PatientRecord();
            r.setPatientId(p.getId());
            r.setFullName(p.getFullName());
            r.setDateOfBirth(p.getDateOfBirth());
            r.setSex(p.getSex());
            r.setPhone(p.getPhone());
            diagnoses.findFirstByPatientOrderByIdDesc(p).ifPresent(d -> {
                r.setWard(d.getWard());
                r.setBed(d.getBed());
                r.setStatus(d.getStatus());
                r.setDisease(d.getDisease());
            });
            return r;
        });
    }

    @Transactional(readOnly = true)
    public long countPatients() {
        return patients.count();
    }

    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return diagnoses.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public long countRegisteredToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        return patients.countByRegisteredAtBetween(start, start.plusDays(1));
    }

    // Saves a patient and their first diagnosis together
    public Diagnosis register(PatientRecord r) {
        Patient p = new Patient();
        copyPatient(p, r);
        patients.save(p);

        Diagnosis d = new Diagnosis();
        d.setPatient(p);
        copyDiagnosis(d, r);
        return diagnoses.save(d);
    }

    // Updates the patient and their latest diagnosis
    public Optional<Diagnosis> update(Long patientId, PatientRecord r) {
        return patients.findById(patientId).map(p -> {
            copyPatient(p, r);
            patients.save(p);

            Diagnosis d = diagnoses.findFirstByPatientOrderByIdDesc(p).orElseGet(Diagnosis::new);
            d.setPatient(p);
            copyDiagnosis(d, r);
            return diagnoses.save(d);
        });
    }

    // Deletes diagnoses first, then the patient (foreign key order)
    public boolean delete(Long patientId) {
        return patients.findById(patientId).map(p -> {
            diagnoses.deleteAll(diagnoses.findByPatient(p));
            patients.delete(p);
            return true;
        }).orElse(false);
    }

    private void copyPatient(Patient p, PatientRecord r) {
        p.setFullName(r.getFullName());
        p.setDateOfBirth(r.getDateOfBirth());
        p.setSex(r.getSex());
        p.setPhone(r.getPhone());
    }

    private void copyDiagnosis(Diagnosis d, PatientRecord r) {
        d.setWard(r.getWard());
        d.setBed(r.getBed());
        d.setStatus(r.getStatus());
        d.setDisease(r.getDisease());
    }
}
