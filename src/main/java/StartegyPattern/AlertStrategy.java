package StartegyPattern;

public interface AlertStrategy {
   
       /**
     * Checks if an alert should be triggered based on the given value.
     * 
     * @param value the value to be checked
     * @return true if an alert should be triggered, otherwise false
     */
   
    boolean checkAlert(double value);
} 
