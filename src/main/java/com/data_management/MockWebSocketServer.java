package com.data_management;

import java.util.ArrayList;
import java.util.List;

public class MockWebSocketServer {
    private List<String> messages;
    private DataStorage dataStorage;
    
    public MockWebSocketServer() {
        this.messages = new ArrayList<>();
        this.dataStorage = new DataStorage();
    }

    // Simulate sending a message from the server
    public void sendMessage(String message) {
        messages.add(message);
    }

    // Get the URI of the mock server
    public String getURI() {
        // Return a mock URI for testing purposes
        return "ws://localhost:8080/mockServer";
    }

    public DataStorage getDataStorage(){
        return dataStorage;
    }
}
