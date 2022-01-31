package com.example.expensetracker.model;

public enum PushNotificationParameter {
    SOUND("default"),
    COLOR("#FFFF00");

    private String value;

    PushNotificationParameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}