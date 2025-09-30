package com.lab.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRecordDTO {
    private Long id;
    private String recordType;
    private String content;
    private String diagnosis;
    private String treatment;
    private String medications;
    private String notes;
    private LocalDateTime recordDate;
    private Long patientId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

