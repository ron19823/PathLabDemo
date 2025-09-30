package com.lab.demo.Controller;

import com.lab.demo.dto.WorkflowDTO;
import com.lab.demo.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/workflows")
@RequiredArgsConstructor
@Log4j2
public class WorkflowController {
    
    private final WorkflowService workflowService;
    
    @GetMapping("/hospital/{hospitalCode}")
    public String getWorkflowsByHospital(@PathVariable String hospitalCode, Model model) {
        log.info("GET /workflows/hospital/{} - Fetching workflows for hospital", hospitalCode);
        List<WorkflowDTO> workflows = workflowService.getWorkflowsByHospitalCode(hospitalCode);
        model.addAttribute("workflows", workflows);
        model.addAttribute("hospitalCode", hospitalCode);
        return "workflows";
    }
    
    @PostMapping("/hospital/{hospitalCode}")
    @ResponseBody
    public ResponseEntity<WorkflowDTO> createWorkflow(@PathVariable String hospitalCode, @RequestBody WorkflowDTO workflowDTO) {
        log.info("POST /workflows/hospital/{} - Creating workflow for hospital", hospitalCode);
        WorkflowDTO createdWorkflow = workflowService.createWorkflow(hospitalCode, workflowDTO);
        return ResponseEntity.ok(createdWorkflow);
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<WorkflowDTO> updateWorkflow(@PathVariable Long id, @RequestBody WorkflowDTO workflowDTO) {
        log.info("PUT /workflows/{} - Updating workflow", id);
        WorkflowDTO updatedWorkflow = workflowService.updateWorkflow(id, workflowDTO);
        return ResponseEntity.ok(updatedWorkflow);
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteWorkflow(@PathVariable Long id) {
        log.info("DELETE /workflows/{} - Deleting workflow", id);
        workflowService.deleteWorkflow(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/api/hospital/{hospitalCode}")
    @ResponseBody
    public ResponseEntity<List<WorkflowDTO>> getWorkflowsByHospitalApi(@PathVariable String hospitalCode) {
        log.info("GET /workflows/api/hospital/{} - Fetching workflows via API", hospitalCode);
        List<WorkflowDTO> workflows = workflowService.getWorkflowsByHospitalCode(hospitalCode);
        return ResponseEntity.ok(workflows);
    }
}

