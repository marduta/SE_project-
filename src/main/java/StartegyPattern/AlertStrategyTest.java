package StartegyPattern;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertStrategyTest {
     @Test
    public void testBloodPressureStrategy() {
        AlertStrategy strategy = new BloodPressureStrategy();
        assertTrue(strategy.checkAlert(150)); // Blood pressure above threshold
        assertFalse(strategy.checkAlert(120)); // Blood pressure below threshold
    }

    @Test
    public void testHeartRateStrategy() {
        AlertStrategy strategy = new HeartRateStrategy();
        assertTrue(strategy.checkAlert(110)); // Heart rate above threshold
        assertFalse(strategy.checkAlert(90)); // Heart rate below threshold
    }

    @Test
    public void testOxygenSaturationStrategy() {
        AlertStrategy strategy = new OxygenSaturationStrategy();
        assertTrue(strategy.checkAlert(85)); // Oxygen saturation below threshold
        assertFalse(strategy.checkAlert(95)); // Oxygen saturation above threshold
    }
}
