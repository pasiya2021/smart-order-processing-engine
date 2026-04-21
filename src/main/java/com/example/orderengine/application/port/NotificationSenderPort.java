package com.example.orderengine.application.port;
import com.example.orderengine.domain.entity.Order;

// ✅ Service talks to THIS — not Email/SMS directly
public interface NotificationSenderPort {
    void send(String recipient, String message);
    String getType(); // returns "EMAIL", "SMS", "PUSH"
}
