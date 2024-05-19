package data_management;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;

import com.cardio_generator.outputs.WebSocketClientImpl;
import com.data_management.DataStorage;

public class WebSocketClientImplTest {
    
    @Mock
    DataStorage mockDataStorage;

    @Before
    public void setUp() throws Exception{
    }

    @Test
    public void testOnOpen(){
        WebSocketClientImpl client = null;
        try {
            client = new WebSocketClientImpl(mockDataStorage, "ws://localhost:8080", false, 3, 1000, 5);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.onOpen(null);
        assertTrue(client.isOpen());
    }

    @Test 
    public void testOnMessageWithValidData(){
        WebSocketClientImpl client = null;
        try {
            client = new WebSocketClientImpl(mockDataStorage, "ws://localhost:8080", true, 3, 1000, 5);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String validJsonMessage = "{\"patientId\":1,\"measurementValue\":75.0,\"recordType\":\"HeartRate\",\"timestamp\":1620518658000}";
        client.onMessage(validJsonMessage);
        verify(mockDataStorage, times(1)).addPatientData(1, 75.0, "HeartRate", 1620518658000L);
    }

    @Test
    public void testOnMessageWithInvalidData() {
        // Test onMessage method with invalid JSON message
        WebSocketClientImpl client = null;
        try {
            client = new WebSocketClientImpl(mockDataStorage, "ws://localhost:8080", true, 3, 1000, 5);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String invalidJsonMessage = "Invalid JSON message";
        client.onMessage(invalidJsonMessage);
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    public void testOnClose() {
        // Test onClose method of WebSocketClientImpl
        WebSocketClientImpl client = null;
        try {
            client = new WebSocketClientImpl(mockDataStorage, "ws://localhost:8080", false, 3, 1000, 5);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.onClose(1000, "Connection closed", true);
        assertFalse(client.isOpen());
    }
    @Test
    public void testOnError() {
        // Test onError method of WebSocketClientImpl
        WebSocketClientImpl client = null;
        try {
            client = new WebSocketClientImpl(mockDataStorage, "ws://localhost:8080", false, 3, 1000, 5);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.onError(new Exception("Error occurred"));
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
        verify(client, times(5)).reconnect(); // Check if reconnect method is called 5 times
        
    }
}
