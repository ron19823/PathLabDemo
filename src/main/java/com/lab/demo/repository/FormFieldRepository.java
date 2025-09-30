package com.lab.demo.repository;

import com.lab.demo.model.FormField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {
    
    List<FormField> findByWorkflowIdAndIsActiveTrueOrderByFieldOrderAsc(Long workflowId);
    
    @Query("SELECT f FROM FormField f WHERE f.workflow.id = :workflowId AND f.isActive = true ORDER BY f.fieldOrder ASC")
    List<FormField> findActiveFieldsByWorkflowId(@Param("workflowId") Long workflowId);
    
    List<FormField> findByWorkflowId(Long workflowId);
}

