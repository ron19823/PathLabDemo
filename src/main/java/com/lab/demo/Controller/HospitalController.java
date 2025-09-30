package com.lab.demo.Controller;

import com.lab.demo.dto.HospitalDTO;
import com.lab.demo.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hospitals")
@RequiredArgsConstructor
@Log4j2
public class HospitalController {
    
    private final HospitalService hospitalService;
    
    @GetMapping
    public String getAllHospitals(Model model) {
        log.info("GET /hospitals - Fetching all hospitals");
        List<HospitalDTO> hospitals = hospitalService.getAllActiveHospitals();
        model.addAttribute("hospitals", hospitals);
        return "hospitals";
    }
    
    @GetMapping("/{code}")
    public String getHospitalByCode(@PathVariable String code, Model model) {
        log.info("GET /hospitals/{} - Fetching hospital by code", code);
        HospitalDTO hospital = hospitalService.getHospitalByCode(code)
                .orElseThrow(() -> new RuntimeException("Hospital not found with code: " + code));
        model.addAttribute("hospital", hospital);
        return "hospital-detail";
    }
    
    @PostMapping
    @ResponseBody
    public ResponseEntity<HospitalDTO> createHospital(@RequestBody HospitalDTO hospitalDTO) {
        log.info("POST /hospitals - Creating new hospital: {}", hospitalDTO.getName());
        HospitalDTO createdHospital = hospitalService.createHospital(hospitalDTO);
        return ResponseEntity.ok(createdHospital);
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<HospitalDTO> updateHospital(@PathVariable Long id, @RequestBody HospitalDTO hospitalDTO) {
        log.info("PUT /hospitals/{} - Updating hospital", id);
        HospitalDTO updatedHospital = hospitalService.updateHospital(id, hospitalDTO);
        return ResponseEntity.ok(updatedHospital);
    }
    
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<HospitalDTO>> getAllHospitalsApi() {
        log.info("GET /hospitals/api - Fetching all hospitals via API");
        List<HospitalDTO> hospitals = hospitalService.getAllActiveHospitals();
        return ResponseEntity.ok(hospitals);
    }
}

