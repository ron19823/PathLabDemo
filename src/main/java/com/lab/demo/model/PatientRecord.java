package com.lab.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String recordType; // "VISIT", "PRESCRIPTION", "TEST_RESULT", "MEDICAL_HISTORY"
    
    @Column(length = 5000)
    private String content; // Main content of the record
    
    @Column(length = 1000)
    private String diagnosis;
    
    @Column(length = 1000)
    private String treatment;
    
    @Column(length = 1000)
    private String medications;
    
    @Column(length = 1000)
    private String notes;
    
    @Column
    private LocalDateTime recordDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

