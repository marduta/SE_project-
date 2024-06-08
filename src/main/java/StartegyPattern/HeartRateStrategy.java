package StartegyPattern;

public class HeartRateStrategy implements AlertStrategy{
        /**
     * Checks if an alert should be triggered based on the heart rate value.
     * 
     * @param value the heart rate value
     * @return true if an alert should be triggered, otherwise false
     */
    
    @Override
    public boolean checkAlert(double value) {
        return value > 100;
    }
}
