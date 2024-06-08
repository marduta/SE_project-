package com.FactoryMethodPattern;

public abstract class Alert {
    protected String patientId;
    protected long timestamp;

    public Alert (String patientId, long timestamp){
        this.patientId = patientId;
        this.timestamp = timestamp;
    }

    public abstract void triggerAlert();
}
