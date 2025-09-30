package com.lab.demo.service;

import com.lab.demo.dto.WorkflowDTO;
import com.lab.demo.dto.FormFieldDTO;
import com.lab.demo.model.Hospital;
import com.lab.demo.model.Workflow;
import com.lab.demo.repository.HospitalRepository;
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
public class WorkflowService {
    
    private final WorkflowRepository workflowRepository;
    private final HospitalRepository hospitalRepository;
    private final FormFieldService formFieldService;
    
    public List<WorkflowDTO> getWorkflowsByHospitalCode(String hospitalCode) {
        log.info("Fetching workflows for hospital code: {}", hospitalCode);
        
        Hospital hospital = hospitalRepository.findByCode(hospitalCode)
                .orElseThrow(() -> new RuntimeException("Hospital not found with code: " + hospitalCode));
        
        return workflowRepository.findActiveWorkflowsByHospitalId(hospital.getId())
                .stream()
                .map(this::convertToDTOWithFields)
                .collect(Collectors.toList());
    }
    
    public List<WorkflowDTO> getWorkflowsByHospitalId(Long hospitalId) {
        log.info("Fetching workflows for hospital ID: {}", hospitalId);
        
        return workflowRepository.findActiveWorkflowsByHospitalId(hospitalId)
                .stream()
                .map(this::convertToDTOWithFields)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public WorkflowDTO createWorkflow(String hospitalCode, WorkflowDTO workflowDTO) {
        log.info("Creating workflow for hospital code: {}", hospitalCode);
        
        Hospital hospital = hospitalRepository.findByCode(hospitalCode)
                .orElseThrow(() -> new RuntimeException("Hospital not found with code: " + hospitalCode));
        
        Workflow workflow = new Workflow();
        workflow.setName(workflowDTO.getName());
        workflow.setDescription(workflowDTO.getDescription());
        workflow.setStepOrder(workflowDTO.getStepOrder());
        workflow.setIsActive(workflowDTO.getIsActive() != null ? workflowDTO.getIsActive() : true);
        workflow.setHospital(hospital);
        
        Workflow savedWorkflow = workflowRepository.save(workflow);
        log.info("Workflow created successfully with ID: {}", savedWorkflow.getId());

        // If incoming DTO contains fields, create them now under this workflow
        if (workflowDTO.getFormFields() != null && !workflowDTO.getFormFields().isEmpty()) {
            for (FormFieldDTO fieldDTO : workflowDTO.getFormFields()) {
                try {
                    formFieldService.createFormField(savedWorkflow.getId(), fieldDTO);
                } catch (Exception ex) {
                    log.error("Failed to create form field '{}' for workflow {}: {}", fieldDTO.getFieldName(), savedWorkflow.getId(), ex.getMessage());
                    throw ex;
                }
            }
        }

        return convertToDTOWithFields(savedWorkflow);
    }
    
    @Transactional
    public WorkflowDTO updateWorkflow(Long id, WorkflowDTO workflowDTO) {
        log.info("Updating workflow with ID: {}", id);
        
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found with ID: " + id));
        
        workflow.setName(workflowDTO.getName());
        workflow.setDescription(workflowDTO.getDescription());
        workflow.setStepOrder(workflowDTO.getStepOrder());
        workflow.setIsActive(workflowDTO.getIsActive());
        
        Workflow savedWorkflow = workflowRepository.save(workflow);
        log.info("Workflow updated successfully");
        
        return convertToDTOWithFields(savedWorkflow);
    }
    
    @Transactional
    public void deleteWorkflow(Long id) {
        log.info("Deleting workflow with ID: {}", id);
        
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found with ID: " + id));
        
        workflow.setIsActive(false);
        workflowRepository.save(workflow);
        log.info("Workflow deactivated successfully");
    }
    
    private WorkflowDTO convertToDTOWithFields(Workflow workflow) {
        WorkflowDTO dto = new WorkflowDTO();
        dto.setId(workflow.getId());
        dto.setName(workflow.getName());
        dto.setDescription(workflow.getDescription());
        dto.setStepOrder(workflow.getStepOrder());
        dto.setIsActive(workflow.getIsActive());
        dto.setHospitalId(workflow.getHospital().getId());
        // populate active form fields for this workflow
        List<FormFieldDTO> fields = formFieldService.getFormFieldsByWorkflowId(workflow.getId());
        dto.setFormFields(fields);
        dto.setCreatedAt(workflow.getCreatedAt());
        dto.setUpdatedAt(workflow.getUpdatedAt());
        return dto;
    }
}

