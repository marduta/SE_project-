package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
/**
 * This interface is implemented by every generator class to accurately generate and label data.
 * 
 * @author Lara
 * 
 */
public interface PatientDataGenerator {
    /**
     * This method takes the patientId and the outputStrategy and checks certain
     * thresholds and unique requirements for every generator: Alert, BloodLevel, BloodPressure etc.
     * then creates the right output(labels the data based on the generator)for that specific situation.
     * 
     * @param patientId
     * @param outputStrategy
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
