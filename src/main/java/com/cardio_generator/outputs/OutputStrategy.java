package com.cardio_generator.outputs;
/**
 * This interface is implemented by every output class(FileOutput, ConsoleOutput, TcpOutput and WebSocketOutput)
 * to accurately format the output.
 * 
 * @author Lara
 * 
 */
public interface OutputStrategy {
    /**
     * This method takes the patientId, time, generated datatype and the data value itself,
     * to print the output message in the wanted format and puts these parameters into the accurate process based on that.
     * 
     * @param patientId
     * @param timestamp
     * @param label
     * @param data
     */
    void output(int patientId, long timestamp, String label, String data);
}
