package com.alerts;

public class RepeatedAlertDecorator implements AlertDecorator {
    private IAlert decoratedAlert;
    private int count;

    public RepeatedAlertDecorator(IAlert decoratedAlert, int count) {
        this.decoratedAlert = decoratedAlert;
        this.count = count;
    }

    @Override
    public void trigger() {
        for (int i = 0; i < count; i++) {
            decoratedAlert.trigger();
        }
    }
    
}
