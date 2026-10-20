package com.hospital.patientregistry.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patients")
public class Patient extends BaseEntity {

    @Column(name = "patient_code", unique = true, nullable = false, length = 20)
    private String patientCode;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Sex is required")
    @Column(nullable = false, length = 10)
    private String sex;

    @Pattern(regexp = "^[0-9+\\- ]{7,15}$", message = "Enter a valid phone number")
    @Column(length = 15)
    private String phone;

    @NotBlank(message = "Ward is required")
    @Column(nullable = false, length = 50)
    private String ward;

    @Column(length = 20)
    private String bed;

    @NotBlank(message = "Status is required")
    @Column(nullable = false, length = 20)
    private String status = "OUTPATIENT";   // ADMITTED or OUTPATIENT

    @PrePersist
    void generatePatientCode() {
        if (patientCode == null) {
            patientCode = "PT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }
    }

    public String getPatientCode() { return patientCode; }
    public void setPatientCode(String patientCode) { this.patientCode = patientCode; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getBed() { return bed; }
    public void setBed(String bed) { this.bed = bed; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}