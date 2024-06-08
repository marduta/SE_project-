package com.alerts;

public class PriorityAlertDecorator implements AlertDecorator {
    private IAlert decoratedAlert;
    private int priorityLevel;

    public PriorityAlertDecorator(IAlert decoratedAlert, int priorityLevel) {
        this.decoratedAlert = decoratedAlert;
        this.priorityLevel = priorityLevel;
    }
    
    @Override
    public void trigger() {
        System.out.println("Priority level: " + priorityLevel);
        decoratedAlert.trigger();
    }
    
}
