package com.lab.demo.repository;

import com.lab.demo.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    
    Optional<Hospital> findByCode(String code);
    
    List<Hospital> findByIsActiveTrue();
    
    @Query("SELECT h FROM Hospital h WHERE h.isActive = true ORDER BY h.name ASC")
    List<Hospital> findAllActiveHospitals();
    
    boolean existsByCode(String code);
    
    boolean existsByName(String name);
}
