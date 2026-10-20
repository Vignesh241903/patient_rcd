package com.hospital.patientregistry.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/*
 * Mapped superclass: its fields become columns in every child table,
 * but it is not a table itself. This is the JPA inheritance feature used here.
 *
 * ID generation options (change the strategy below to demonstrate them):
 *   IDENTITY  - database auto-increment column (used here, works with PostgreSQL)
 *   SEQUENCE  - database sequence object
 *   TABLE     - a separate table stores the counter (portable, slower)
 *   AUTO      - Hibernate chooses based on the database
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @PrePersist
    void setRegistrationTime() {
        if (registeredAt == null) {
            registeredAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
}