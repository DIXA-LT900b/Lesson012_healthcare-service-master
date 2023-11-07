package ru.dixa.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import ru.dixa.patient.entity.BloodPressure;
import ru.dixa.patient.entity.HealthInfo;
import ru.dixa.patient.entity.PatientInfo;
import ru.dixa.patient.entity.*;
import ru.dixa.patient.repository.PatientInfoFileRepository;
import ru.dixa.patient.repository.PatientInfoRepository;
import ru.dixa.patient.service.alert.SendAlertService;
import ru.dixa.patient.service.alert.SendAlertServiceImpl;
import ru.dixa.patient.service.medical.MedicalService;
import ru.dixa.patient.service.medical.MedicalServiceImpl;

public class Main {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        File repoFile = new File("patients.txt");
        PatientInfoRepository patientInfoRepository = new PatientInfoFileRepository(repoFile, mapper);

        String id1 = patientInfoRepository.add(
            new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
        );

        String id2 = patientInfoRepository.add(
            new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
        );

        SendAlertService alertService = new SendAlertServiceImpl();
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        //run service
        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalService.checkBloodPressure(id1, currentPressure);

        BigDecimal currentTemperature = new BigDecimal("37.9");
        medicalService.checkTemperature(id1, currentTemperature);
    }
}
