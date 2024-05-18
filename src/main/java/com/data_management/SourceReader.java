package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SourceReader implements DataReader {

    private String output_dir;

    public SourceReader(String output_dir) {
        this.output_dir = output_dir;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        File directory = new File(output_dir);
        if(directory.exists() == false) {
            throw new IllegalArgumentException("This output directory does not exist: " + output_dir);
        }
        else {
            File[] patientFiles = directory.listFiles();
            if(patientFiles != null) {
                for(File patientFile : patientFiles) {
                    parsePatientFile(patientFile, dataStorage);
                }
            }
        }
    }

    private void parsePatientFile(File patienFile, DataStorage storage) {
        try(BufferedReader reader = new BufferedReader(new FileReader(patienFile))) {
            String row;
            while((row = reader.readLine()) != null) {
                PatientRecord record = parseLine(row);
                if(record != null) {
                    storage.addPatientData(record.getPatientId(), record.getMeasurementValue(), record.getRecordType(), record.getTimestamp());
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private PatientRecord parseLine(String row) {
        String[] columns = row.split(","); // Assuming the raw data is in CSV format.
        if(columns.length != 4) {
            return null;
        }
    int patientId = Integer.parseInt(columns[0]);
    double measurementValue = Double.parseDouble(columns[1]);
    String recordType = columns[2];
    long timestamp = Long.parseLong(columns[3]);

    return new PatientRecord(patientId, measurementValue, recordType, timestamp);
    }
    
}
