package com.hospital.patientregistry.model;

import jakarta.persistence.*;

@Entity
@Table(name = "diagnosis")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links to patient.patient_code
    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_code", referencedColumnName = "patient_code", nullable = false)
    private Patient patient;

    @Column(nullable = false, length = 50)
    private String ward;

    @Column(nullable = false, length = 20)
    private String bed;

    @Column(nullable = false, length = 20)
    private String status;     // ADMITTED or OUTPATIENT

    @Column(nullable = false, length = 150)
    private String disease;

    // Outpatients have no ward or bed, so they are stored as "-"
    @PrePersist
    @PreUpdate
    void normalizeWardAndBed() {
        if ("OUTPATIENT".equals(status) || ward == null || ward.isBlank()) {
            ward = "-";
        }
        if ("OUTPATIENT".equals(status) || bed == null || bed.isBlank()) {
            bed = "-";
        }
    }

    public Long getId() { return id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getBed() { return bed; }
    public void setBed(String bed) { this.bed = bed; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }
}
