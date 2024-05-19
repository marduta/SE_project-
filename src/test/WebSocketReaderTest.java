import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.WebSocketSourceReader;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WebSocketReaderTest {
    private static final int TEST_PORT = 8887; // Adjustable.
    private static final String TEST_SERVER_URI = "ws://localhost:" + TEST_PORT; // Adjustable.
    private WebSocketServer server;
    private DataStorage storage;

    @Before
    public void setup() throws Exception {
        storage = new DataStorage();
        server = new WebSocketServer(new InetSocketAddress(TEST_PORT)) {
            @Override
            public void onOpen(WebSocket connection, ServerHandshake handshake) {
                System.out.println("New Connection.");
            }
            @Override
            public void onClose(WebSocket connection, int code, String reason, boolean remote) {
                System.out.println("Connection Closed.");
            }

            @Override
            public void onMessage(WebSocket connection, String message) {
                System.out.println("A Message is Received:" + message);
                connection.send(message);
            }

            @Override
            public void onError(WebSocket connection, Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onStart() {
                System.out.println("Server Started Successfully.");
            }
        };
        server.start();
        Thread.sleep(550); //Adding a delay to make sure the server has started.
    }

    @After
    public void tearDown() throws Exception { // This method makes sure to stop the server after each test.
        if (webSocketServer != null) {
            webSocketServer.stop();
        }
    }

    @Test
    public void testWebSocketSourceReader() throws Exception {
        // Create a WebSocketSourceReader object with the test server URI and data storage.
        WebSocketSourceReader reader = new WebSocketSourceReader(TEST_SERVER_URI, storage);

        // Connect the reader to the server.
        reader.connect();

        // Testing sending a message from the server to the reader.
        String testMessage = "1,98.6,HeartRate,1643527200000"; // Sample CSV message
        server.connections().forEach(connection -> connection.send(testMessage));

        Thread.sleep(2000); //Wait for message processing.

        // Check that the data was stored correctly in storage.
        PatientRecord record = storage.getRecords(1, 0, Long.MAX_VALUE).get(0);

        assertNotNull(record);
        assertEquals(1, record.getPatientId());
        assertEquals(98.6, record.getMeasurementValue(), 0.001);
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(1643527200000L, record.getTimestamp());

        // Disconnect from the reader.
        reader.disconnect();
    }
}
