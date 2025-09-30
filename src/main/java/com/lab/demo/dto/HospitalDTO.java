package com.lab.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String address;
    private String city;
    private String state;
    private String country;
    private String phone;
    private String email;
    private Boolean isActive;
    private List<WorkflowDTO> workflows;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

