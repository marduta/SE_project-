package com.FactoryMethodPattern;

public class ECGAlert extends Alert {
       /**
     * Constructs an ECG alert with the specified patient ID and timestamp.
     * 
     * @param patientId the ID of the patient associated with the alert
     * @param timestamp the timestamp when the alert was triggered
     */
    
    public ECGAlert(String patientId, long timestamp) {
        super(patientId, timestamp);
    }

      /**
     * Triggers the ECG alert, notifying healthcare providers and sending notifications.
     */
    @Override
    public void triggerAlert() {
       
        System.out.println("ECG Alert triggered for patient: " + patientId);
        
       
        HealthcareProvider.notifyHealthcareProviders(patientId, "ECG Alert", timestamp);
        
        
        NotificationService.sendNotification(patientId, "ECG Alert", timestamp);
    }
}
