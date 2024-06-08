package com.FactoryMethodPattern;

    /**
     * Creates an alert based on the specified condition.
     * 
     * @param patientId the ID of the patient
     * @param condition the condition for which the alert is generated
     * @param timestamp the timestamp when the alert is generated
     * @return an instance of Alert corresponding to the condition
     */

public abstract class AlertFactory {
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}
