package com.FactoryMethodPattern;

public class BloodOxygenAlert extends Alert {
       /**
     * Constructs a new BloodOxygenAlert object with the specified patient ID and timestamp.
     * 
     * @param patientId the ID of the patient for whom the alert is triggered
     * @param timestamp the timestamp when the alert is triggered
     */
    
    public BloodOxygenAlert(String patientId, long timestamp){
        super(patientId, timestamp);
    }

       /**
     * Triggers the blood oxygen alert and notifies healthcare providers.
     */
    @Override
    public void triggerAlert() {
       
        System.out.println("Blood Oxygen Alert triggered for patient: " + patientId);
        
       
        HealthcareProvider.notifyHealthcareProviders(patientId, "Blood Oxygen Alert", timestamp);
        
        
        NotificationService.sendNotification(patientId, "Blood Oxygen Alert", timestamp);
    }

}
