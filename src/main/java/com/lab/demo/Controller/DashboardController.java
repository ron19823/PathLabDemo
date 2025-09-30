package com.lab.demo.Controller;

import com.lab.demo.dto.HospitalDTO;
import com.lab.demo.dto.WorkflowDTO;
import com.lab.demo.service.HospitalService;
import com.lab.demo.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@Log4j2
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final HospitalService hospitalService;
    private final WorkflowService workflowService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(@RequestParam(required = false) String hospitalCode, Model model, Authentication authentication) {
        log.info("GET /dashboard called with hospitalCode: {}", hospitalCode);
        
        if (hospitalCode != null && !hospitalCode.isEmpty()) {
            // Hospital-specific dashboard
            HospitalDTO hospital = hospitalService.getHospitalByCode(hospitalCode)
                    .orElseThrow(() -> new RuntimeException("Hospital not found with code: " + hospitalCode));
            
            List<WorkflowDTO> workflows = workflowService.getWorkflowsByHospitalCode(hospitalCode);
            
            model.addAttribute("hospital", hospital);
            model.addAttribute("workflows", workflows);
            model.addAttribute("isHospitalSpecific", true);
            // expose role-gated flag for UI builder
            boolean canDesign = hasAnyRole(authentication, Set.of("ROLE_ADMIN", "ROLE_DESIGNER"));
            model.addAttribute("canDesign", canDesign);
        } else {
            // General dashboard - show hospital selection
            List<HospitalDTO> hospitals = hospitalService.getAllActiveHospitals();
            model.addAttribute("hospitals", hospitals);
            model.addAttribute("isHospitalSpecific", false);
        }
        
        return "dashboard";
    }
    
    @GetMapping("/hospital/{hospitalCode}")
    public String hospitalDashboard(@PathVariable String hospitalCode, Model model, Authentication authentication) {
        log.info("GET /hospital/{} - Hospital-specific dashboard", hospitalCode);
        return dashboard(hospitalCode, model, authentication);
    }

    private boolean hasAnyRole(Authentication authentication, Set<String> roles) {
        if (authentication == null) return false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (roles.contains(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}