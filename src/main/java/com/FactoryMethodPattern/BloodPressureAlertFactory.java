package com.FactoryMethodPattern;

public class BloodPressureAlertFactory extends AlertFactory {
    
        /**
     * Creates a blood pressure alert.
     * 
     * @param patientId the ID of the patient
     * @param condition the condition for which the alert is generated
     * @param timestamp the timestamp when the alert is generated
     * @return an instance of BloodPressureAlert
     */
    
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        if (condition.equals("blood_pressure_anomaly")) {
            return new BloodPressureAlert(patientId, timestamp);
        }
        return null;
    }
}
