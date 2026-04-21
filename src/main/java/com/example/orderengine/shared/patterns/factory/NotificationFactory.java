package com.example.orderengine.shared.patterns.factory;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationFactory {

    private final Map<String, NotificationSender> senders;

    // ✅ Spring injects ALL NotificationSender implementations automatically
    public NotificationFactory(List<NotificationSender> senderList) {
        this.senders = senderList.stream()
                .collect(Collectors.toMap(NotificationSender::getType, s -> s));
    }

    // ✅ Give me type → I give you the right sender
    public NotificationSender getSender(String type) {
        NotificationSender sender = senders.get(type.toUpperCase());
        if (sender == null) {
            throw new IllegalArgumentException("Unknown notification type: " + type);
        }
        return sender;
    }
}