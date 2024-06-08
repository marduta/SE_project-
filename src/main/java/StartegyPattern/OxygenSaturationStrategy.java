package StartegyPattern;

public class OxygenSaturationStrategy implements AlertStrategy{
    
    /**
     * Checks if an alert should be triggered based on the oxygen saturation value.
     * 
     * @param value the oxygen saturation value
     * @return true if an alert should be triggered, otherwise false
     */
    @Override
    public boolean checkAlert(double value) {
        return value < 90;
    }
}
