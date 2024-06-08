package com.FactoryMethodPattern;

public class NotificationService {
    public static void sendNotification(String patientId, String alertType, long timestamp) {
       
        System.out.println("Sending notification to patient " + patientId + " about " + alertType);
       
    }
}
