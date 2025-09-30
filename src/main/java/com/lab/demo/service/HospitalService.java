package com.lab.demo.service;

import com.lab.demo.dto.HospitalDTO;
import com.lab.demo.model.Hospital;
import com.lab.demo.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class HospitalService {
    
    private final HospitalRepository hospitalRepository;
    
    public List<HospitalDTO> getAllActiveHospitals() {
        log.info("Fetching all active hospitals");
        return hospitalRepository.findAllActiveHospitals()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<HospitalDTO> getHospitalByCode(String code) {
        log.info("Fetching hospital by code: {}", code);
        return hospitalRepository.findByCode(code)
                .map(this::convertToDTO);
    }
    
    public Optional<Hospital> getHospitalEntityByCode(String code) {
        log.info("Fetching hospital entity by code: {}", code);
        return hospitalRepository.findByCode(code);
    }
    
    @Transactional
    public HospitalDTO createHospital(HospitalDTO hospitalDTO) {
        log.info("Creating new hospital: {}", hospitalDTO.getName());
        
        Hospital hospital = new Hospital();
        hospital.setName(hospitalDTO.getName());
        hospital.setCode(hospitalDTO.getCode());
        hospital.setDescription(hospitalDTO.getDescription());
        hospital.setAddress(hospitalDTO.getAddress());
        hospital.setCity(hospitalDTO.getCity());
        hospital.setState(hospitalDTO.getState());
        hospital.setCountry(hospitalDTO.getCountry());
        hospital.setPhone(hospitalDTO.getPhone());
        hospital.setEmail(hospitalDTO.getEmail());
        hospital.setIsActive(hospitalDTO.getIsActive() != null ? hospitalDTO.getIsActive() : true);
        
        Hospital savedHospital = hospitalRepository.save(hospital);
        log.info("Hospital created successfully with ID: {}", savedHospital.getId());
        
        return convertToDTO(savedHospital);
    }
    
    @Transactional
    public HospitalDTO updateHospital(Long id, HospitalDTO hospitalDTO) {
        log.info("Updating hospital with ID: {}", id);
        
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found with ID: " + id));
        
        hospital.setName(hospitalDTO.getName());
        hospital.setCode(hospitalDTO.getCode());
        hospital.setDescription(hospitalDTO.getDescription());
        hospital.setAddress(hospitalDTO.getAddress());
        hospital.setCity(hospitalDTO.getCity());
        hospital.setState(hospitalDTO.getState());
        hospital.setCountry(hospitalDTO.getCountry());
        hospital.setPhone(hospitalDTO.getPhone());
        hospital.setEmail(hospitalDTO.getEmail());
        hospital.setIsActive(hospitalDTO.getIsActive());
        
        Hospital savedHospital = hospitalRepository.save(hospital);
        log.info("Hospital updated successfully");
        
        return convertToDTO(savedHospital);
    }
    
    public boolean existsByCode(String code) {
        return hospitalRepository.existsByCode(code);
    }
    
    public boolean existsByName(String name) {
        return hospitalRepository.existsByName(name);
    }
    
    private HospitalDTO convertToDTO(Hospital hospital) {
        HospitalDTO dto = new HospitalDTO();
        dto.setId(hospital.getId());
        dto.setName(hospital.getName());
        dto.setCode(hospital.getCode());
        dto.setDescription(hospital.getDescription());
        dto.setAddress(hospital.getAddress());
        dto.setCity(hospital.getCity());
        dto.setState(hospital.getState());
        dto.setCountry(hospital.getCountry());
        dto.setPhone(hospital.getPhone());
        dto.setEmail(hospital.getEmail());
        dto.setIsActive(hospital.getIsActive());
        dto.setCreatedAt(hospital.getCreatedAt());
        dto.setUpdatedAt(hospital.getUpdatedAt());
        return dto;
    }
}

