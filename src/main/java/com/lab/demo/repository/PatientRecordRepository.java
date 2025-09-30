package com.lab.demo.repository;

import com.lab.demo.model.PatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRecordRepository extends JpaRepository<PatientRecord, Long> {
    
    @Query("SELECT pr FROM PatientRecord pr WHERE pr.patient.id = :patientId ORDER BY pr.recordDate DESC, pr.createdAt DESC")
    List<PatientRecord> findByPatientIdOrderByRecordDateDesc(@Param("patientId") Long patientId);
    
    List<PatientRecord> findByPatientIdAndRecordType(Long patientId, String recordType);
}
