package com.example.expensetracker.dtos;

public class PushNotificationRequest {

    private String title;
    private String message;
    private String topic;
    private String token;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private PushNotificationRequest(PushNotificationRequest.Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.topic = builder.topic;
        this.token = builder.token;
    }

    public static PushNotificationRequest.Builder builder() {
        return new PushNotificationRequest.Builder();
    }

    public static class Builder {
        private String title;
        private String message;
        private String topic;
        private String token;

        private Builder() {
        }

        public PushNotificationRequest.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public PushNotificationRequest.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public PushNotificationRequest.Builder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public PushNotificationRequest.Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public PushNotificationRequest build() {
            return new PushNotificationRequest(this);
        }
    }
}
