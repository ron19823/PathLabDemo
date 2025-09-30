package com.lab.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String patientId; // Hospital-specific patient ID
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private Integer age;
    
    @Column
    private String bloodGroup;
    
    @Column
    private String idNumber; // Aadhar, PAN, etc.
    
    @Column(length = 5000)
    private String customFields; // JSON string for dynamic fields
    
    @Column(length = 1000)
    private String medicalHistory;
    
    @Column(length = 1000)
    private String prescriptions;
    
    @Column(length = 1000)
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientRecord> records;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

