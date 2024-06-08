package com.FactoryMethodPattern;

import java.util.List;

public class HealthcareProvider {
        /**
     * Notifies healthcare providers about an alert for a specific patient.
     * 
     * @param patientId the ID of the patient associated with the alert
     * @param alertType the type of alert triggered
     * @param timestamp the timestamp when the alert was triggered
     */
    public static void notifyHealthcareProviders(String patientId, String alertType, long timestamp) {
        
        List<String> healthcareProviders = Database.getHealthcareProviders(patientId);
        for (String provider : healthcareProviders) {
            System.out.println("Notifying healthcare provider " + provider + " about " + alertType + " for patient " + patientId);
        }
    }
}
