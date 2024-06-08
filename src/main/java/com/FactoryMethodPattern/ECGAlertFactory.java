package com.FactoryMethodPattern;

public class ECGAlertFactory extends AlertFactory{
       /**
     * Creates an ECG alert based on the specified condition.
     * 
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition triggering the alert
     * @param timestamp the timestamp when the alert was triggered
     * @return an instance of ECGAlert if the condition is "irregular_heartbeat", otherwise null
     */
    
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        if (condition.equals("irregular_heartbeat")) {
            return new ECGAlert(patientId, timestamp);
        }
        return null; 
    }
}
