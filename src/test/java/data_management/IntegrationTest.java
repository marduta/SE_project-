package data_management;

import org.junit.Assert;
import org.junit.Test;

import com.cardio_generator.outputs.WebSocketClientImpl;
import com.data_management.DataStorage;
import com.data_management.MockWebSocketServer;
import com.data_management.PatientRecord;

import java.net.URISyntaxException;
import java.util.List;

public class IntegrationTest<WebSocketSocketImpl> {
    @Test
    public void testWebSocketIntegration(){
        MockWebSocketServer mockServer = new MockWebSocketServer();
        WebSocketClientImpl client = null;

        try {
            client = new WebSocketClientImpl(new DataStorage(), mockServer.getURI(), false, 3, 1000, 5);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //send mock messages from the server
        String mockMessage1 = "{\"patientId\":1,\"measurementValue\":75.0,\"recordType\":\"HeartRate\",\"timestamp\":1620518658000}";
        String mockMessage2 = "{\"patientId\":2,\"measurementValue\":120.0,\"recordType\":\"BloodPressure\",\"timestamp\":1620518658001}";
        mockServer.sendMessage(mockMessage1);
        mockServer.sendMessage(mockMessage2);

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        //verify if WebSocketClientImpl correctly processes and stores data
        DataStorage dataStorage = client.getDataStorage();
        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);
        Assert.assertEquals(1, records.size());
        PatientRecord record = records.get(0);
        Assert.assertEquals(1, record.getPatientId());
        Assert.assertEquals("HeartRate", record.getRecordType());
        Assert.assertEquals(75.0, record.getMeasurementValue(), 0.01);
        
    }
}
