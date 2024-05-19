package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

public class WebSocketSourceReader implements DataReader {
    private WebSocketClient client;
    private DataStorage storage;
    private String server;

    public WebSocketSourceReader(String server, DataStorage storage) throws URISyntaxException {
        this.server = server;
        this.storage = storage;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {} // Not used.

    @Override
    public void connect() throws IOException {
        try {
            client = new WebSocketClient(new URI(server)) { // First we create a new WebSocketClient object with the server URI.
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connected to server."); // When the connection is successful, It prints "Connected to server."
                }
    
                @Override
                public void onMessage(String message) { //When a data is received, it parses the line and stores the record in dataStorage.
                    PatientRecord record = parseLine(message);
                    if (record != null) {
                        storage.addPatientData(record.getPatientId(), record.getMeasurementValue(), record.getRecordType(), record.getTimestamp());
                    }
                }
    
                @Override
                public void onClose(int code, String reason, boolean remote) { // "int code" is the status code indicating the reason for the connection closure.
                // and "boolean remote" indicates whether on not the connection was closed by the server or the actual client.
                    System.out.println("Connection is closed: " + reason); // When the connection is closed, It prints "Connection is closed: " with the appropriate reason.
                }
    
                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };
            client.connect();
        } catch (URISyntaxException e) {
            throw new IOException("Wrong websocket:", e);
        }
    }

    @Override
    public void disconnect() { // It's purpose is closing the WebSocket connection if it is open.
        if(client != null) {
            client.close();
        }
    }

    @Override
    public PatientRecord parseLine(String row) {
        String[] columns = row.split(","); // Assuming the raw data is in CSV format.
        if (columns.length != 4) {
            return null;
        }
        int patientId = Integer.parseInt(columns[0]);
        double measurementValue = Double.parseDouble(columns[1]);
        String recordType = columns[2];
        long timestamp = Long.parseLong(columns[3]);
    
        return new PatientRecord(patientId, measurementValue, recordType, timestamp);
    }

    
}
