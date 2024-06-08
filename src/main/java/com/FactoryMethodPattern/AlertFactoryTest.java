import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


package com.FactoryMethodPattern;

public class AlertFactoryTest {
    @Test
    public void testBloodPressureAlertCreation() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("123", "blood_pressure_anomaly", System.currentTimeMillis());
        assertNotNull(alert);
        assertTrue(alert instanceof BloodPressureAlert);
    }

    @Test
    public void testBloodOxygenAlertCreation() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("456", "blood_oxygen_change", System.currentTimeMillis());
        assertNotNull(alert);
        assertTrue(alert instanceof BloodOxygenAlert);
    }

    @Test
    public void testECGAlertCreation() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("789", "irregular_heartbeat", System.currentTimeMillis());
        assertNotNull(alert);
        assertTrue(alert instanceof ECGAlert);
    }
}
