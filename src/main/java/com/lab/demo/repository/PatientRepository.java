package com.lab.demo.repository;

import com.lab.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    List<Patient> findByHospitalId(Long hospitalId);
    
    @Query("SELECT p FROM Patient p WHERE p.hospital.id = :hospitalId ORDER BY p.createdAt DESC")
    List<Patient> findByHospitalIdOrderByCreatedAtDesc(@Param("hospitalId") Long hospitalId);
    
    Optional<Patient> findByHospitalIdAndPatientId(Long hospitalId, String patientId);
    
    @Query("SELECT p FROM Patient p WHERE p.hospital.id = :hospitalId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.patientId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.idNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Patient> searchPatients(@Param("hospitalId") Long hospitalId, @Param("searchTerm") String searchTerm);
}

