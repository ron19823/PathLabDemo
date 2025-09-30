package com.lab.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDTO {
    private Long id;
    private String name;
    private String description;
    private Integer stepOrder;
    private Boolean isActive;
    private Long hospitalId;
    private List<FormFieldDTO> formFields;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
