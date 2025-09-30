package com.lab.demo.service;

import com.lab.demo.dto.FormFieldDTO;
import com.lab.demo.dto.HospitalDTO;
import com.lab.demo.dto.WorkflowDTO;
import com.lab.demo.model.FormField;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class DataInitializationService implements CommandLineRunner {
    
    private final HospitalService hospitalService;
    private final WorkflowService workflowService;
    private final FormFieldService formFieldService;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Initializing sample data...");
        
        // Create sample hospitals
        createSampleHospitals();
        
        log.info("Sample data initialization completed");
    }
    
    private void createSampleHospitals() {
        // Hospital 1: Apollo Hospital (Aadhar required)
        if (!hospitalService.existsByCode("APOLLO")) {
            HospitalDTO apollo = new HospitalDTO();
            apollo.setName("Apollo Hospital");
            apollo.setCode("APOLLO");
            apollo.setDescription("Leading multi-specialty hospital");
            apollo.setAddress("123 Medical District");
            apollo.setCity("Mumbai");
            apollo.setState("Maharashtra");
            apollo.setCountry("India");
            apollo.setPhone("+91-22-12345678");
            apollo.setEmail("info@apollo.com");
            apollo.setIsActive(true);
            
            HospitalDTO savedApollo = hospitalService.createHospital(apollo);
            createApolloWorkflows(savedApollo.getId());
        }
        
        // Hospital 2: City Hospital (No Aadhar required)
        if (!hospitalService.existsByCode("CITY")) {
            HospitalDTO city = new HospitalDTO();
            city.setName("City General Hospital");
            city.setCode("CITY");
            city.setDescription("Community hospital serving local area");
            city.setAddress("456 Community Street");
            city.setCity("Delhi");
            city.setState("Delhi");
            city.setCountry("India");
            city.setPhone("+91-11-87654321");
            city.setEmail("info@cityhospital.com");
            city.setIsActive(true);
            
            HospitalDTO savedCity = hospitalService.createHospital(city);
            createCityWorkflows(savedCity.getId());
        }
        
        // Hospital 3: Metro Hospital (Complex workflow)
        if (!hospitalService.existsByCode("METRO")) {
            HospitalDTO metro = new HospitalDTO();
            metro.setName("Metro Medical Center");
            metro.setCode("METRO");
            metro.setDescription("Advanced medical center with specialized departments");
            metro.setAddress("789 Healthcare Avenue");
            metro.setCity("Bangalore");
            metro.setState("Karnataka");
            metro.setCountry("India");
            metro.setPhone("+91-80-11223344");
            metro.setEmail("info@metro.com");
            metro.setIsActive(true);
            
            HospitalDTO savedMetro = hospitalService.createHospital(metro);
            createMetroWorkflows(savedMetro.getId());
        }
    }
    
    private void createApolloWorkflows(Long hospitalId) {
        // Basic Registration Workflow
        WorkflowDTO basicWorkflow = new WorkflowDTO();
        basicWorkflow.setName("Patient Registration");
        basicWorkflow.setDescription("Basic patient registration with Aadhar verification");
        basicWorkflow.setStepOrder(1);
        basicWorkflow.setIsActive(true);
        basicWorkflow.setHospitalId(hospitalId);
        
        WorkflowDTO savedWorkflow = workflowService.createWorkflow("APOLLO", basicWorkflow);
        
        // Create form fields for Apollo
        createApolloFormFields(savedWorkflow.getId());
    }
    
    private void createApolloFormFields(Long workflowId) {
        // Name field
        FormFieldDTO nameField = new FormFieldDTO();
        nameField.setFieldName("name");
        nameField.setDisplayName("Full Name");
        nameField.setFieldType(FormField.FieldType.TEXT);
        nameField.setIsRequired(true);
        nameField.setFieldOrder(1);
        nameField.setPlaceholder("Enter your full name");
        nameField.setHelpText("Please enter your complete name as per government ID");
        nameField.setIsActive(true);
        nameField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, nameField);
        
        // Age field
        FormFieldDTO ageField = new FormFieldDTO();
        ageField.setFieldName("age");
        ageField.setDisplayName("Age");
        ageField.setFieldType(FormField.FieldType.NUMBER);
        ageField.setIsRequired(true);
        ageField.setFieldOrder(2);
        ageField.setPlaceholder("Enter your age");
        ageField.setHelpText("Age in years");
        ageField.setIsActive(true);
        ageField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, ageField);
        
        // Aadhar field (required for Apollo)
        FormFieldDTO aadharField = new FormFieldDTO();
        aadharField.setFieldName("aadhar");
        aadharField.setDisplayName("Aadhar Number");
        aadharField.setFieldType(FormField.FieldType.TEXT);
        aadharField.setIsRequired(true);
        aadharField.setFieldOrder(3);
        aadharField.setPlaceholder("Enter 12-digit Aadhar number");
        aadharField.setHelpText("Aadhar number is mandatory for registration");
        aadharField.setValidationRules("{\"pattern\":\"^[0-9]{12}$\",\"message\":\"Aadhar must be 12 digits\"}");
        aadharField.setIsActive(true);
        aadharField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, aadharField);
        
        // Blood Group field
        FormFieldDTO bloodGroupField = new FormFieldDTO();
        bloodGroupField.setFieldName("bloodGroup");
        bloodGroupField.setDisplayName("Blood Group");
        bloodGroupField.setFieldType(FormField.FieldType.SELECT);
        bloodGroupField.setIsRequired(true);
        bloodGroupField.setFieldOrder(4);
        bloodGroupField.setOptions("[\"A+\",\"A-\",\"B+\",\"B-\",\"AB+\",\"AB-\",\"O+\",\"O-\"]");
        bloodGroupField.setIsActive(true);
        bloodGroupField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, bloodGroupField);
        
        // Phone field
        FormFieldDTO phoneField = new FormFieldDTO();
        phoneField.setFieldName("phone");
        phoneField.setDisplayName("Phone Number");
        phoneField.setFieldType(FormField.FieldType.PHONE);
        phoneField.setIsRequired(true);
        phoneField.setFieldOrder(5);
        phoneField.setPlaceholder("Enter 10-digit phone number");
        phoneField.setHelpText("Primary contact number");
        phoneField.setIsActive(true);
        phoneField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, phoneField);
    }
    
    private void createCityWorkflows(Long hospitalId) {
        // Simple Registration Workflow (No Aadhar)
        WorkflowDTO simpleWorkflow = new WorkflowDTO();
        simpleWorkflow.setName("Patient Registration");
        simpleWorkflow.setDescription("Simple patient registration without Aadhar");
        simpleWorkflow.setStepOrder(1);
        simpleWorkflow.setIsActive(true);
        simpleWorkflow.setHospitalId(hospitalId);
        
        WorkflowDTO savedWorkflow = workflowService.createWorkflow("CITY", simpleWorkflow);
        
        // Create form fields for City Hospital
        createCityFormFields(savedWorkflow.getId());
    }
    
    private void createCityFormFields(Long workflowId) {
        // Name field
        FormFieldDTO nameField = new FormFieldDTO();
        nameField.setFieldName("name");
        nameField.setDisplayName("Full Name");
        nameField.setFieldType(FormField.FieldType.TEXT);
        nameField.setIsRequired(true);
        nameField.setFieldOrder(1);
        nameField.setPlaceholder("Enter your full name");
        nameField.setIsActive(true);
        nameField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, nameField);
        
        // Age field
        FormFieldDTO ageField = new FormFieldDTO();
        ageField.setFieldName("age");
        ageField.setDisplayName("Age");
        ageField.setFieldType(FormField.FieldType.NUMBER);
        ageField.setIsRequired(true);
        ageField.setFieldOrder(2);
        ageField.setPlaceholder("Enter your age");
        ageField.setIsActive(true);
        ageField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, ageField);
        
        // ID Number field (optional)
        FormFieldDTO idField = new FormFieldDTO();
        idField.setFieldName("idNumber");
        idField.setDisplayName("ID Number (Optional)");
        idField.setFieldType(FormField.FieldType.TEXT);
        idField.setIsRequired(false);
        idField.setFieldOrder(3);
        idField.setPlaceholder("Aadhar, PAN, or other ID");
        idField.setHelpText("Any government ID (optional)");
        idField.setIsActive(true);
        idField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, idField);
        
        // Blood Group field
        FormFieldDTO bloodGroupField = new FormFieldDTO();
        bloodGroupField.setFieldName("bloodGroup");
        bloodGroupField.setDisplayName("Blood Group");
        bloodGroupField.setFieldType(FormField.FieldType.SELECT);
        bloodGroupField.setIsRequired(false);
        bloodGroupField.setFieldOrder(4);
        bloodGroupField.setOptions("[\"A+\",\"A-\",\"B+\",\"B-\",\"AB+\",\"AB-\",\"O+\",\"O-\",\"Unknown\"]");
        bloodGroupField.setIsActive(true);
        bloodGroupField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, bloodGroupField);
    }
    
    private void createMetroWorkflows(Long hospitalId) {
        // Step 1: Basic Information
        WorkflowDTO basicWorkflow = new WorkflowDTO();
        basicWorkflow.setName("Basic Information");
        basicWorkflow.setDescription("Step 1: Basic patient information");
        basicWorkflow.setStepOrder(1);
        basicWorkflow.setIsActive(true);
        basicWorkflow.setHospitalId(hospitalId);
        
        WorkflowDTO savedBasicWorkflow = workflowService.createWorkflow("METRO", basicWorkflow);
        createMetroBasicFields(savedBasicWorkflow.getId());
        
        // Step 2: Medical History
        WorkflowDTO medicalWorkflow = new WorkflowDTO();
        medicalWorkflow.setName("Medical History");
        medicalWorkflow.setDescription("Step 2: Medical history and current conditions");
        medicalWorkflow.setStepOrder(2);
        medicalWorkflow.setIsActive(true);
        medicalWorkflow.setHospitalId(hospitalId);
        
        WorkflowDTO savedMedicalWorkflow = workflowService.createWorkflow("METRO", medicalWorkflow);
        createMetroMedicalFields(savedMedicalWorkflow.getId());
        
        // Step 3: Emergency Contact
        WorkflowDTO emergencyWorkflow = new WorkflowDTO();
        emergencyWorkflow.setName("Emergency Contact");
        emergencyWorkflow.setDescription("Step 3: Emergency contact information");
        emergencyWorkflow.setStepOrder(3);
        emergencyWorkflow.setIsActive(true);
        emergencyWorkflow.setHospitalId(hospitalId);
        
        WorkflowDTO savedEmergencyWorkflow = workflowService.createWorkflow("METRO", emergencyWorkflow);
        createMetroEmergencyFields(savedEmergencyWorkflow.getId());
    }
    
    private void createMetroBasicFields(Long workflowId) {
        // Name field
        FormFieldDTO nameField = new FormFieldDTO();
        nameField.setFieldName("name");
        nameField.setDisplayName("Full Name");
        nameField.setFieldType(FormField.FieldType.TEXT);
        nameField.setIsRequired(true);
        nameField.setFieldOrder(1);
        nameField.setPlaceholder("Enter your full name");
        nameField.setIsActive(true);
        nameField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, nameField);
        
        // Date of Birth field
        FormFieldDTO dobField = new FormFieldDTO();
        dobField.setFieldName("dateOfBirth");
        dobField.setDisplayName("Date of Birth");
        dobField.setFieldType(FormField.FieldType.DATE);
        dobField.setIsRequired(true);
        dobField.setFieldOrder(2);
        dobField.setIsActive(true);
        dobField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, dobField);
        
        // Gender field
        FormFieldDTO genderField = new FormFieldDTO();
        genderField.setFieldName("gender");
        genderField.setDisplayName("Gender");
        genderField.setFieldType(FormField.FieldType.SELECT);
        genderField.setIsRequired(true);
        genderField.setFieldOrder(3);
        genderField.setOptions("[\"Male\",\"Female\",\"Other\",\"Prefer not to say\"]");
        genderField.setIsActive(true);
        genderField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, genderField);
        
        // Aadhar field
        FormFieldDTO aadharField = new FormFieldDTO();
        aadharField.setFieldName("aadhar");
        aadharField.setDisplayName("Aadhar Number");
        aadharField.setFieldType(FormField.FieldType.TEXT);
        aadharField.setIsRequired(true);
        aadharField.setFieldOrder(4);
        aadharField.setPlaceholder("Enter 12-digit Aadhar number");
        aadharField.setValidationRules("{\"pattern\":\"^[0-9]{12}$\",\"message\":\"Aadhar must be 12 digits\"}");
        aadharField.setIsActive(true);
        aadharField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, aadharField);
    }
    
    private void createMetroMedicalFields(Long workflowId) {
        // Blood Group field
        FormFieldDTO bloodGroupField = new FormFieldDTO();
        bloodGroupField.setFieldName("bloodGroup");
        bloodGroupField.setDisplayName("Blood Group");
        bloodGroupField.setFieldType(FormField.FieldType.SELECT);
        bloodGroupField.setIsRequired(true);
        bloodGroupField.setFieldOrder(1);
        bloodGroupField.setOptions("[\"A+\",\"A-\",\"B+\",\"B-\",\"AB+\",\"AB-\",\"O+\",\"O-\"]");
        bloodGroupField.setIsActive(true);
        bloodGroupField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, bloodGroupField);
        
        // Allergies field
        FormFieldDTO allergiesField = new FormFieldDTO();
        allergiesField.setFieldName("allergies");
        allergiesField.setDisplayName("Known Allergies");
        allergiesField.setFieldType(FormField.FieldType.TEXTAREA);
        allergiesField.setIsRequired(false);
        allergiesField.setFieldOrder(2);
        allergiesField.setPlaceholder("List any known allergies");
        allergiesField.setHelpText("Include food, drug, or environmental allergies");
        allergiesField.setIsActive(true);
        allergiesField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, allergiesField);
        
        // Current Medications field
        FormFieldDTO medicationsField = new FormFieldDTO();
        medicationsField.setFieldName("currentMedications");
        medicationsField.setDisplayName("Current Medications");
        medicationsField.setFieldType(FormField.FieldType.TEXTAREA);
        medicationsField.setIsRequired(false);
        medicationsField.setFieldOrder(3);
        medicationsField.setPlaceholder("List current medications");
        medicationsField.setHelpText("Include dosage and frequency");
        medicationsField.setIsActive(true);
        medicationsField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, medicationsField);
        
        // Medical History field
        FormFieldDTO historyField = new FormFieldDTO();
        historyField.setFieldName("medicalHistory");
        historyField.setDisplayName("Medical History");
        historyField.setFieldType(FormField.FieldType.TEXTAREA);
        historyField.setIsRequired(false);
        historyField.setFieldOrder(4);
        historyField.setPlaceholder("Previous surgeries, conditions, etc.");
        historyField.setIsActive(true);
        historyField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, historyField);
    }
    
    private void createMetroEmergencyFields(Long workflowId) {
        // Emergency Contact Name
        FormFieldDTO contactNameField = new FormFieldDTO();
        contactNameField.setFieldName("emergencyContactName");
        contactNameField.setDisplayName("Emergency Contact Name");
        contactNameField.setFieldType(FormField.FieldType.TEXT);
        contactNameField.setIsRequired(true);
        contactNameField.setFieldOrder(1);
        contactNameField.setPlaceholder("Full name of emergency contact");
        contactNameField.setIsActive(true);
        contactNameField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, contactNameField);
        
        // Emergency Contact Phone
        FormFieldDTO contactPhoneField = new FormFieldDTO();
        contactPhoneField.setFieldName("emergencyContactPhone");
        contactPhoneField.setDisplayName("Emergency Contact Phone");
        contactPhoneField.setFieldType(FormField.FieldType.PHONE);
        contactPhoneField.setIsRequired(true);
        contactPhoneField.setFieldOrder(2);
        contactPhoneField.setPlaceholder("10-digit phone number");
        contactPhoneField.setIsActive(true);
        contactPhoneField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, contactPhoneField);
        
        // Relationship
        FormFieldDTO relationshipField = new FormFieldDTO();
        relationshipField.setFieldName("relationship");
        relationshipField.setDisplayName("Relationship");
        relationshipField.setFieldType(FormField.FieldType.SELECT);
        relationshipField.setIsRequired(true);
        relationshipField.setFieldOrder(3);
        relationshipField.setOptions("[\"Spouse\",\"Parent\",\"Child\",\"Sibling\",\"Friend\",\"Other\"]");
        relationshipField.setIsActive(true);
        relationshipField.setWorkflowId(workflowId);
        formFieldService.createFormField(workflowId, relationshipField);
    }
}

