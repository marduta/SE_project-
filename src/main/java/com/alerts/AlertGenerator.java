package com.alerts;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiChannel;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List <PatientRecord> records = patient.getRecords(0L, System.currentTimeMillis());
        for (PatientRecord record : records)
            checkAlertConditions(record);
    }

    private void checkAlertConditions(PatientRecord record){
        String recordType = record.getRecordType();
        if (recordType.equalsIgnoreCase("BloodPressure"))
            checkBloodPressureAlert(record);
        if (recordType.equalsIgnoreCase("BloodOxigenSaturation"))
            checkBloodSaturationAlert(record);
    }

    private void checkBloodPressureAlert (PatientRecord record){
        
        checkCriticalThresholdAlert(record);
        checkBloodPressureTrend(record);
    }

    private void checkCriticalThresholdAlert(PatientRecord record){
    
        //extract systolic and diastolic values from the record

        double bloodPressure = record.getMeasurementValue();
        double systolic = Double.parseDouble(String.valueOf((int)bloodPressure));
        double diastolic = Double.parseDouble(String.valueOf((int)((bloodPressure - systolic)*100)));

        //check for critical thresholds
        if (systolic > 180 || systolic < 90 || diastolic > 120 || diastolic < 60){
            String condition = "Blood Pressure Critical Threshold exceeded: Systolic= " + systolic + ", Diastolic= " + diastolic;
            triggerAlert(new Alert(String.valueOf(record.getPatientId()), condition, System.currentTimeMillis()));
        }
    }

    private void checkBloodPressureTrend(PatientRecord record){
        
        //track blood pressure readings for trend analysis
        List<Double> recentReadings = new ArrayList<>();
        recentReadings.add(Double.parseDouble(String.valueOf((int)record.getMeasurementValue())));
        recentReadings.add(Double.parseDouble(String.valueOf((int)((record.getMeasurementValue() - Double.parseDouble(String.valueOf((int)record.getMeasurementValue()))) *100))));
        
        //loop through 2 previous records(max) to identify trends
        List <PatientRecord> previousRecords = new Patient(record.getPatientId()).getRecords(record.getTimestamp() - (2 * 1000), record.getTimestamp());
        for (PatientRecord prevRecord : previousRecords){
            if (prevRecord.getRecordType().equalsIgnoreCase("BloodPressure")){
                recentReadings.add(Double.parseDouble(String.valueOf((int)prevRecord.getMeasurementValue())));
                recentReadings.add(Double.parseDouble(String.valueOf((int)((prevRecord.getMeasurementValue() - Double.parseDouble(String.valueOf((int)prevRecord.getMeasurementValue()))) *100))));
            }
        }

        //analyze trend based on the last 3 readings
        if (recentReadings.size() == 6){
            double systolicChange1 = recentReadings.get(1) - recentReadings.get(3);
            double systolicChange2 = recentReadings.get(3) - recentReadings.get(5);
            double diastolicChange1 = recentReadings.get(2) - recentReadings.get(4);
            double diastolicChange2 = recentReadings.get(4) - recentReadings.get(6);
            
            //check for chenges in either systolic or diastolic 
            boolean hasTrend = Math.abs(systolicChange1) > 10 || Math.abs(systolicChange2) > 10 || Math.abs(diastolicChange1) > 10 || Math.abs(diastolicChange2) > 10;
            String trend ="";
            if (hasTrend){
                if ((systolicChange1 > 0 && systolicChange2 > 0) || (diastolicChange1 > 0 && diastolicChange2 > 0))
                    trend = "Blood Pressure Increasing trend";
                if ((systolicChange1 < 0 && systolicChange2 < 0) ||(diastolicChange1 < 0 && diastolicChange2 < 0))
                    trend = "Blood Pressure decreasing Trend";
             
        }
            if (!trend.equals(""))
            triggerAlert(new Alert(String.valueOf(record.getPatientId()), trend, System.currentTimeMillis()));
            
        }
    }
   
    private void checkBloodSaturationAlert(PatientRecord record){
        checkLowSaturationAlert(record);
        checkRapidDropAlert(record);
    }

    private void checkLowSaturationAlert (PatientRecord record){
        double saturation = record.getMeasurementValue();
        if (saturation < 92)
            triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Blood Oxygen Saturation Below 92% (" + saturation + "%)", System.currentTimeMillis()));
    }

    private void checkRapidDropAlert(PatientRecord record){
        //maintain a history of readings with timestamps
        List<BloodSaturationReading> saturationHistory = getBloodSaturationHistory(record.getPatientId());
        if (saturationHistory.isEmpty()){
            //no history, add current reading and skip rapid drop check
            saturationHistory.add(new BloodSaturationReading(saturation, record.getTimestamp()));
            return;
        }

        BloodSaturationReading previousReading = saturationHistory.get(saturationHistory.size()-1);
        long timeDifference = record.getTimestamp() - previousReading.getTimestamp();
        final int MINUTES10 = 600*1000;
        if (timeDifference <= MINUTES10){
            double saturationDifference = record.getMeasurementValue() - previousReading.getMeasurementValue();
            double saturationDropPercent = (saturationDifference / previousReading.getValue()) * 100;
            if (saturationDropPercent >= 5)
                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Blood Oxygen Saturation Rapid Drop By " + String.format("%.2f", saturationDropPercent) + "%", System.currentTimeMillis()));
        }

        final int MAX_HISTORY_SIZE = 1000000; 
        //update saturation history with current reading (assuming limited size)
        if (saturationHistory.size() == MAX_HISTORY_SIZE)
            saturationHistory.remove(0);//remove oldest reading if history is full
        saturationHistory.add(new BloodSaturationReading(record.getMeasurementValue(), record.getTimestamp()));
    }
    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("Alert Triggered:");
        System.out.println("Patient ID:" + alert.getPatientId());
        System.out.println("Description: " + alert.getCondition());
        System.out.println("Timestamp: " + alert.getTimestamp());
    }
}
