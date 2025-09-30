package com.lab.demo.dto;

import com.lab.demo.model.FormField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldDTO {
    private Long id;
    private String fieldName;
    private String displayName;
    private FormField.FieldType fieldType;
    private Boolean isRequired;
    private Integer fieldOrder;
    private String validationRules;
    private String options;
    private String placeholder;
    private String helpText;
    private Boolean isActive;
    private Long workflowId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

