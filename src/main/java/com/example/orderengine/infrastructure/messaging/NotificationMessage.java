package com.example.orderengine.infrastructure.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// ✅ This is what gets sent through RabbitMQ queue as JSON
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage implements Serializable {
    private Long orderId;
    private String customerName;
    private String productName;
    private String status;
    private String type;        // EMAIL, SMS, PUSH
    private String recipient;   // email address or phone
}
