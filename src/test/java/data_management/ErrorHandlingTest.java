package data_management;

import static org.junit.Assert.*;

import java.net.URISyntaxException;

import org.junit.Test;

import com.cardio_generator.outputs.WebSocketClientImpl;
import com.data_management.MockWebSocketServer;

public class ErrorHandlingTest {

    @Test
    public void testNetworkErrorHandling() throws NetworkException{
        try {
            MockWebSocketServer mockServer = new MockWebSocketServer();
            WebSocketClientImpl client = new WebSocketClientImpl(mockServer.getDataStorage(), "ws://localhost:8080", false, 3, 1000, 5);
            client.connect();
            fail("Expected NetworkException");
        } catch (URISyntaxException e) {
            assertEquals("Network error occurred", e.getMessage());
        }
    }

    @Test
    public void testDataTransmissionFailureHandling() throws DataTransmissionException, URISyntaxException{
        
            // Simulate a data transmission failure by sending an invalid message
            MockWebSocketServer mockServer = new MockWebSocketServer();
            WebSocketClientImpl client = new WebSocketClientImpl(mockServer.getDataStorage(), "ws://localhost:8080", false, 3, 1000, 5);
            mockServer.sendMessage("Invalid JSON message");
           
    }

    /**
     * InnerErrorHandlingTest
     */
    private class NetworkException extends Exception {
        @SuppressWarnings("unused")
        public NetworkException(String message){
            super(message);
        }
        
    }

    private class DataTransmissionException extends Exception {
        @SuppressWarnings("unused")
        public DataTransmissionException(String message){
            super(message);
        }
    } 
    
}
