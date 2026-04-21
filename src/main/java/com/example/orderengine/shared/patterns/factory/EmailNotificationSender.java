package com.example.orderengine.shared.patterns.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationSender implements NotificationSender {

    @Override
    public void send(String recipient, String message) {
        // real SMTP logic goes here later
        log.info("📧 EMAIL to {} → {}", recipient, message);
    }

    @Override
    public String getType() { return "EMAIL"; }
}