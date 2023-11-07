package ru.dixa.patient.repository;

import ru.dixa.patient.entity.PatientInfo;

public interface PatientInfoRepository {

    PatientInfo getById(String id);

    String add(PatientInfo patientInfo);

    PatientInfo remove(String id);

    PatientInfo update(PatientInfo patientInfo);
}
