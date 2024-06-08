package com.FactoryMethodPattern;

public class BloodPressureAlert extends Alert{
       /**
     * Constructs a BloodPressureAlert object with the specified patient ID and timestamp.
     * 
     * @param patientId the ID of the patient for whom the alert is triggered
     * @param timestamp the timestamp when the alert is triggered
     */
    public BloodPressureAlert(String patientId, long timestamp) {
        super(patientId, timestamp);
    }

       /**
     * Triggers the Blood Pressure Alert and notifies healthcare providers.
     */
    @Override
    public void triggerAlert() {
       
        System.out.println("Blood Pressure Alert triggered for patient: " + patientId);
        
        
        HealthcareProvider.notifyHealthcareProviders(patientId, "Blood Pressure Alert", timestamp);
        
       
        NotificationService.sendNotification(patientId, "Blood Pressure Alert", timestamp);
    }
}
