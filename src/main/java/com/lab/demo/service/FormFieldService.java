package com.lab.demo.service;

import com.lab.demo.dto.FormFieldDTO;
import com.lab.demo.model.FormField;
import com.lab.demo.model.Workflow;
import com.lab.demo.repository.FormFieldRepository;
import com.lab.demo.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class FormFieldService {
    
    private final FormFieldRepository formFieldRepository;
    private final WorkflowRepository workflowRepository;
    
    public List<FormFieldDTO> getFormFieldsByWorkflowId(Long workflowId) {
        log.info("Fetching form fields for workflow ID: {}", workflowId);
        
        return formFieldRepository.findActiveFieldsByWorkflowId(workflowId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public FormFieldDTO createFormField(Long workflowId, FormFieldDTO formFieldDTO) {
        log.info("Creating form field for workflow ID: {}", workflowId);
        
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new RuntimeException("Workflow not found with ID: " + workflowId));
        
        FormField formField = new FormField();
        formField.setFieldName(formFieldDTO.getFieldName());
        formField.setDisplayName(formFieldDTO.getDisplayName());
        formField.setFieldType(formFieldDTO.getFieldType());
        formField.setIsRequired(formFieldDTO.getIsRequired() != null ? formFieldDTO.getIsRequired() : false);
        formField.setFieldOrder(formFieldDTO.getFieldOrder());
        formField.setValidationRules(formFieldDTO.getValidationRules());
        formField.setOptions(formFieldDTO.getOptions());
        formField.setPlaceholder(formFieldDTO.getPlaceholder());
        formField.setHelpText(formFieldDTO.getHelpText());
        formField.setIsActive(formFieldDTO.getIsActive() != null ? formFieldDTO.getIsActive() : true);
        formField.setWorkflow(workflow);
        
        FormField savedFormField = formFieldRepository.save(formField);
        log.info("Form field created successfully with ID: {}", savedFormField.getId());
        
        return convertToDTO(savedFormField);
    }
    
    @Transactional
    public FormFieldDTO updateFormField(Long id, FormFieldDTO formFieldDTO) {
        log.info("Updating form field with ID: {}", id);
        
        FormField formField = formFieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Form field not found with ID: " + id));
        
        formField.setFieldName(formFieldDTO.getFieldName());
        formField.setDisplayName(formFieldDTO.getDisplayName());
        formField.setFieldType(formFieldDTO.getFieldType());
        formField.setIsRequired(formFieldDTO.getIsRequired());
        formField.setFieldOrder(formFieldDTO.getFieldOrder());
        formField.setValidationRules(formFieldDTO.getValidationRules());
        formField.setOptions(formFieldDTO.getOptions());
        formField.setPlaceholder(formFieldDTO.getPlaceholder());
        formField.setHelpText(formFieldDTO.getHelpText());
        formField.setIsActive(formFieldDTO.getIsActive());
        
        FormField savedFormField = formFieldRepository.save(formField);
        log.info("Form field updated successfully");
        
        return convertToDTO(savedFormField);
    }
    
    @Transactional
    public void deleteFormField(Long id) {
        log.info("Deleting form field with ID: {}", id);
        
        FormField formField = formFieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Form field not found with ID: " + id));
        
        formField.setIsActive(false);
        formFieldRepository.save(formField);
        log.info("Form field deactivated successfully");
    }
    
    private FormFieldDTO convertToDTO(FormField formField) {
        FormFieldDTO dto = new FormFieldDTO();
        dto.setId(formField.getId());
        dto.setFieldName(formField.getFieldName());
        dto.setDisplayName(formField.getDisplayName());
        dto.setFieldType(formField.getFieldType());
        dto.setIsRequired(formField.getIsRequired());
        dto.setFieldOrder(formField.getFieldOrder());
        dto.setValidationRules(formField.getValidationRules());
        dto.setOptions(formField.getOptions());
        dto.setPlaceholder(formField.getPlaceholder());
        dto.setHelpText(formField.getHelpText());
        dto.setIsActive(formField.getIsActive());
        dto.setWorkflowId(formField.getWorkflow().getId());
        dto.setCreatedAt(formField.getCreatedAt());
        dto.setUpdatedAt(formField.getUpdatedAt());
        return dto;
    }
}

