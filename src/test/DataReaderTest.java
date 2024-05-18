package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.DataReader;
import com.data_management.DataStorage;

public class DataReaderTest {
    private DataStorage storage;
    private DataReader reader;

    @BeforeEach
    public void setUp() {
        storage = new DataStorage();
        reader = new DataReader(storage);
    }

    @test
    public void testReader() {
        reader.readData("path/to/test/data"); // Change here when it's test time.
        assertEquals(1, storage.getRecords(1, startTime, endTime).size());
    }
}
