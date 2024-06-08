package StartegyPattern;

public class BloodPressureStrategy implements AlertStrategy{
   
       /**
     * Checks if an alert should be triggered based on the blood pressure value.
     * 
     * @param value the blood pressure value
     * @return true if an alert should be triggered, otherwise false
     */
   
    @Override
    public boolean checkAlert(double value) {
        return value > 140;
    }
}
