package com.example.orderengine.shared.patterns.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PushNotificationSender implements NotificationSender {

    @Override
    public void send(String recipient, String message) {
        log.info("🔔 PUSH to {} → {}", recipient, message);
    }

    @Override
    public String getType() { return "PUSH"; }
}