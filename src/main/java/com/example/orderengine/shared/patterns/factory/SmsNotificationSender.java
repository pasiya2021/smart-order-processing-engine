package com.example.orderengine.shared.patterns.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsNotificationSender implements NotificationSender {

    @Override
    public void send(String recipient, String message) {
        // real SMS API logic goes here later
        log.info("📱 SMS to {} → {}", recipient, message);
    }

    @Override
    public String getType() { return "SMS"; }
}