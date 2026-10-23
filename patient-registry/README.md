# Patient Registry

Spring Boot + JPA/Hibernate + PostgreSQL + Thymeleaf.

Tables: `patient` (id, regis_at, name, dob, sex, phone, patient_code) and
`diagnosis` (id, patient_code, ward, bed, status, disease).

## Run with Docker
    docker compose up --build
Open http://localhost:8080

## Run locally
1. Create database: CREATE DATABASE patient_registry;
   (If an old `patients` table exists: DROP TABLE IF EXISTS patients;)
2. Set DB_USER / DB_PASSWORD if not using postgres/postgres.
3. mvn spring-boot:run

## REST API
GET/POST /api/patients, GET/PUT/DELETE /api/patients/{id}
