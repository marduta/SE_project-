package com.FactoryMethodPattern;

import java.util.List;

public class Database {
     
        /**
     * Retrieves a list of healthcare providers associated with the specified patient ID.
     * 
     * @param patientId the ID of the patient for whom healthcare providers are retrieved
     * @return a list of healthcare providers
     */
     
        public static List<String> getHealthcareProviders(String patientId) {
        
        return List.of("Doctor123", "Nurse456");
}
}
