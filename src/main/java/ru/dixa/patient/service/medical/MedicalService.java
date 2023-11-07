package ru.dixa.patient.service.medical;

import java.math.BigDecimal;

import ru.dixa.patient.entity.BloodPressure;

public interface MedicalService {

    void checkBloodPressure(String patientId, BloodPressure bloodPressure);

    void checkTemperature(String patientId, BigDecimal temperature);
}
