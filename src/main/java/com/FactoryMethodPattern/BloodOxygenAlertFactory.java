package com.FactoryMethodPattern;

public class BloodOxygenAlertFactory extends AlertFactory{
    /**
     * Creates a BloodOxygenAlert object based on the given condition.
     * 
     * @param patientId the ID of the patient for whom the alert is created
     * @param condition the condition triggering the alert
     * @param timestamp the timestamp when the alert is triggered
     * @return a BloodOxygenAlert object if the condition matches, otherwise null
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        if (condition.equals("blood_oxygen_change")) {
            return new BloodOxygenAlert(patientId, timestamp);
        }
        return null; 
    }
}
