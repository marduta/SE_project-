package com.cardio_generator.outputs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.data_management.DataStorage;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * This class implements the WebSocketClient interface and connects to a WebSocket server.
 * It receives messages from the server and stores the parsed data in a DataStorage object.
 */

public class WebSocketClientImpl extends WebSocketClient{
    private DataStorage dataStorage;
    private final boolean useJsonParsing;//flag indicating if JSON parsing is used

    private final int maxReconnects;//max number of reconnection attempts
    private final int maxRetries;// maximum number of retries for failed messages
    private final long retryDelay;//delay between retries in milliseconds
    /**
     * Constructor that takes the DataStorage object, server URI, and a flag indicating whether JSON parsing should be used.
     *
     * @param dataStorage The DataStorage object where the parsed data will be stored.
     * @param serverUri The URI of the WebSocket server to connect to.
     * @param useJsonParsing A flag indicating whether the messages from the server are in JSON format.
     * @throws URISyntaxException If there's an error parsing the server URI.
     */
    public WebSocketClientImpl (DataStorage dataStorage, String serverUri, boolean useJsonParsing, int maxRetries, long retryDelay, int maxReconnects) throws URISyntaxException{
        super(new URI(serverUri));
        this.dataStorage = dataStorage;
        this.useJsonParsing = useJsonParsing;//set the flag
        this.maxRetries = maxRetries;
        this.retryDelay = retryDelay;
        this.maxReconnects = maxReconnects;
    }

     /**
     * Called when the connection to the WebSocket server is opened.
     *
     * @param handshakedata The handshake data from the server.
     */
    @Override
    public void onOpen(ServerHandshake handshakedata){
        System.out.println("Connected to WebSocket server");

    }

    /**
     * Called when a message is received from the WebSocket server.
     *
     * @param message The message received from the server.
     */
    @Override
    public void onMessage (String message){
        System.out.println("Received message: " + message);
        int retryCount = 0;//track number of retries
        while (retryCount < maxRetries){
        try{
            if (useJsonParsing){
                //parse the message as a JSON object
                ObjectMapper objectMapper = new ObjectMapper();
                @SuppressWarnings("unchecked")
                Map<String, Object> dataMap = objectMapper.readValue(message, Map.class);

                //extrcat relevant data
                int patientId = (int) dataMap.get("patientId");
                double measurementValue = (double) dataMap.get("measurementValue");
                String recordType = (String) dataMap.get("recordType");
                long timestamp = (long) dataMap.get("timestamp");

                //store the parsed data using DataStorage
                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            }
            else{
                //assuming message is comma-separated (CSV format)

                String[] parts = message.split(",");
                if (parts.length != 4){
                    System.err.println("Invalid message format: Expected 4 comma-separated values");
                    continue;
                }

                try {
                    int patientId = Integer.parseInt(parts[0]);
                    double measurementValue = Double.parseDouble(parts[1]);
                    String recordType = parts[2];
                    long timestamp = Long.parseLong(parts[3]);

                    //store the parsed data in datastorage
                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing message data: " + e.getMessage());
                }
            }
            //if parsing is successful, break the loop
            break;
        }
        catch (Exception e){
            System.err.println("Error parsing message (retry " + (retryCount + 1) + "): " + e.getMessage());
            retryCount++;
        }

        if(retryCount == maxRetries){
            System.err.println("Failed to parse message after " + maxRetries + " retries");
        }
        else{
            try {
                Thread.sleep(retryDelay);//wait before retrying
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    }

    /**
     * Called when the connection to the WebSocket server is closed.
     *
     * @param code The closing code from the server.
     * @param reason The reason for the closure.
     * @param remote Whether the connection was closed remotely.
     */
    @Override
    public void onClose(int code, String reason, boolean remote){
        System.out.println("Connection closed: " + reason);
    }

    /**
     * Called when an error occurs during the WebSocket communication.
     *
     * @param e The exception that occurred.
     */
    @Override
    public void onError (Exception e){
        System.err.println("Error during communication: " + e.getMessage());
        int reconnectCount = 0;
        while (reconnectCount < maxReconnects){
            try {
                this.close(); //close the existing connection 
                Thread.sleep(retryDelay * (reconnectCount + 1));//increase delay between retries
                this.connect(); 
                System.out.println("Reconnected to the WebSocket server");
                return;
            } catch (Exception ex) {
                System.err.println("Failed to reconnect attempt: " + (reconnectCount + 1));
            }
        }

        System.err.println("Failed to reconnect after " + maxReconnects + " attempts");
        System.out.println("Connection to server lost");
    }

    public DataStorage getDataStorage() {
       return dataStorage;
    }

}

