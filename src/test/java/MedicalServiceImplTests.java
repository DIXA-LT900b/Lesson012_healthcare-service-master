import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.dixa.patient.entity.BloodPressure;
import ru.dixa.patient.entity.HealthInfo;
import ru.dixa.patient.entity.PatientInfo;
import ru.dixa.patient.repository.PatientInfoFileRepository;
import ru.dixa.patient.service.alert.SendAlertServiceImpl;
import ru.dixa.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTests {

    PatientInfo patient1Info = new PatientInfo("112","Иван", "Васильев",
            LocalDate.of(1985,6,22),
            new HealthInfo(new BigDecimal(36.6), new BloodPressure(120,80)));


    @Test
    void warningPressureAndTemperatureTest() {
        // Заглушка для PatientInfoFileRepository
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById("112"))
                .thenReturn(patient1Info);

        // Заглушка для SendAlertServiceImpl
        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        // Создаем тестируемый объект
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);

        // Вызываем методы
        medicalService.checkBloodPressure("112", new BloodPressure(150,120));
        medicalService.checkTemperature("112", new BigDecimal(39.6)); // если указать 36.6 - все равно кидает alert

        // Объявляем перехватчики
        ArgumentCaptor<String> resultPressureMessage = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> resultTemperatureMessage = ArgumentCaptor.forClass(String.class);

        // Задаем триггеры для перехватчиков
        Mockito.verify(sendAlertService).send(resultPressureMessage.capture());
        Mockito.verify(sendAlertService).send(resultTemperatureMessage.capture());


        String expectedMessage = "Warning, patient with id: 112, need help";


        Assertions.assertEquals(expectedMessage, resultPressureMessage.getValue());
        Assertions.assertEquals(expectedMessage, resultTemperatureMessage.getValue());

    }

    @Test
    void normalPressureAndTemperatureTest() {
        // Заглушка для PatientInfoFileRepository
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById("112"))
                .thenReturn(patient1Info);

        // Заглушка для SendAlertServiceImpl
        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        // Создаем тестируемый объект
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);

        // Вызываем методы
        medicalService.checkBloodPressure("112", new BloodPressure(120,80));
        medicalService.checkTemperature("112", new BigDecimal(36.6));

        // метод send не должен вызываться
        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.any());

    }


}
