package com.lab.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "form_fields")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormField {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fieldName; // e.g., "name", "aadhar", "phone"
    
    @Column(nullable = false)
    private String displayName; // e.g., "Full Name", "Aadhar Number", "Phone Number"
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldType fieldType; // TEXT, NUMBER, EMAIL, PHONE, DATE, SELECT, TEXTAREA
    
    @Column(nullable = false)
    private Boolean isRequired = false;
    
    @Column(nullable = false)
    private Integer fieldOrder; // Order within the workflow step
    
    @Column(length = 1000)
    private String validationRules; // JSON string for validation rules
    
    @Column(length = 1000)
    private String options; // JSON string for select options
    
    @Column(length = 1000)
    private String placeholder;
    
    @Column(length = 1000)
    private String helpText;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum FieldType {
        TEXT, NUMBER, EMAIL, PHONE, DATE, SELECT, TEXTAREA, PASSWORD
    }
}

