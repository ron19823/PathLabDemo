package com.lab.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String patientId;
    private String name;
    private Integer age;
    private String bloodGroup;
    private String idNumber;
    private Map<String, Object> customFields;
    private String medicalHistory;
    private String prescriptions;
    private String notes;
    private Long hospitalId;
    private String hospitalName;
    private List<PatientRecordDTO> records;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

