package com.example.orderengine.shared.patterns.factory;

// Every notification type must follow this contract
public interface NotificationSender {
    void send(String recipient, String message);
    String getType(); // EMAIL, SMS, PUSH
}
