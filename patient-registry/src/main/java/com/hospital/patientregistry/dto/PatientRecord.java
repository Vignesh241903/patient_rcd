package com.hospital.patientregistry.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/*
 * One form/API payload holding both patient and diagnosis fields.
 * Validation rules live here, so the entities stay simple.
 */
public class PatientRecord {

    private Long patientId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Sex is required")
    private String sex;

    @Pattern(regexp = "^[0-9+\\- ]{7,15}$", message = "Enter a valid phone number")
    private String phone;

    @NotBlank(message = "Disease is required")
    private String disease;

    @NotBlank(message = "Status is required")
    private String status = "OUTPATIENT";

    private String ward;
    private String bed;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getBed() { return bed; }
    public void setBed(String bed) { this.bed = bed; }
}
