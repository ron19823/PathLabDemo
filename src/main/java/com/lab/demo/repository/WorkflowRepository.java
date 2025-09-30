package com.lab.demo.repository;

import com.lab.demo.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    
    List<Workflow> findByHospitalIdAndIsActiveTrueOrderByStepOrderAsc(Long hospitalId);
    
    @Query("SELECT w FROM Workflow w WHERE w.hospital.id = :hospitalId AND w.isActive = true ORDER BY w.stepOrder ASC")
    List<Workflow> findActiveWorkflowsByHospitalId(@Param("hospitalId") Long hospitalId);
    
    List<Workflow> findByHospitalId(Long hospitalId);
}

