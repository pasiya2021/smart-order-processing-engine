package com.example.orderengine.infrastructure.messaging;

import com.example.orderengine.shared.patterns.factory.NotificationFactory;
import com.example.orderengine.shared.patterns.factory.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationFactory notificationFactory; // ✅ Factory pattern

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(NotificationMessage message) {
        log.info("📥 Received from queue → Order #{}", message.getOrderId());

        // ✅ Factory picks the right sender — no if/else needed
        NotificationSender sender = notificationFactory.getSender(message.getType());

        String text = String.format("Order #%d (%s) is now %s",
                message.getOrderId(),
                message.getProductName(),
                message.getStatus());

        sender.send(message.getRecipient(), text);
    }
}