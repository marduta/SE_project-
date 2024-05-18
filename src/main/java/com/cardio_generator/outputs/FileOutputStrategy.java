package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class creates File outputs in a specified directory.
 * Every unique label corresponds to a different seperate file inside the base directory.
 * 
 * @author Lara
 * 
 */
public class FileOutputStrategy implements OutputStrategy {

    // Changed variable name "BaseDirectory" to camelCase version "baseDirectory".
    private String baseDirectory;

    // Changed HashMap name "file_map" to camelCase version "fileMap".
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * This method takes the patientId, time, generated datatype and the data value itself,
     * to print the output message in the file format based on the data label.
     *
     * @param patientId 
     * @param timestamp 
     * @param label
     * @param data
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // Changed variable name "FilePath" to camelCase version "filePath".
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (IOException e) { // Changed the generic Exception to more specific IOException because it covers file-related errors.
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}