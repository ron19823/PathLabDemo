package com.lab.demo.Controller;

import com.lab.demo.dto.FormFieldDTO;
import com.lab.demo.service.FormFieldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/form-fields")
@RequiredArgsConstructor
@Log4j2
public class FormFieldController {
    
    private final FormFieldService formFieldService;
    
    @GetMapping("/workflow/{workflowId}")
    public String getFormFieldsByWorkflow(@PathVariable Long workflowId, Model model) {
        log.info("GET /form-fields/workflow/{} - Fetching form fields for workflow", workflowId);
        List<FormFieldDTO> formFields = formFieldService.getFormFieldsByWorkflowId(workflowId);
        model.addAttribute("formFields", formFields);
        model.addAttribute("workflowId", workflowId);
        return "form-fields";
    }
    
    @PostMapping("/workflow/{workflowId}")
    @ResponseBody
    public ResponseEntity<FormFieldDTO> createFormField(@PathVariable Long workflowId, @RequestBody FormFieldDTO formFieldDTO) {
        log.info("POST /form-fields/workflow/{} - Creating form field for workflow", workflowId);
        FormFieldDTO createdFormField = formFieldService.createFormField(workflowId, formFieldDTO);
        return ResponseEntity.ok(createdFormField);
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<FormFieldDTO> updateFormField(@PathVariable Long id, @RequestBody FormFieldDTO formFieldDTO) {
        log.info("PUT /form-fields/{} - Updating form field", id);
        FormFieldDTO updatedFormField = formFieldService.updateFormField(id, formFieldDTO);
        return ResponseEntity.ok(updatedFormField);
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteFormField(@PathVariable Long id) {
        log.info("DELETE /form-fields/{} - Deleting form field", id);
        formFieldService.deleteFormField(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/api/workflow/{workflowId}")
    @ResponseBody
    public ResponseEntity<List<FormFieldDTO>> getFormFieldsByWorkflowApi(@PathVariable Long workflowId) {
        log.info("GET /form-fields/api/workflow/{} - Fetching form fields via API", workflowId);
        List<FormFieldDTO> formFields = formFieldService.getFormFieldsByWorkflowId(workflowId);
        return ResponseEntity.ok(formFields);
    }
}

