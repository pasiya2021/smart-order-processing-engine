package com.example.orderengine.shared.patterns.observer;

import com.example.orderengine.domain.entity.Order;
import com.example.orderengine.domain.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements OrderObserver {

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus newStatus) {
        // later this will trigger real email via RabbitMQ
        System.out.println("📧 EMAIL sent to " + order.getCustomerName()
                + " → Order #" + order.getId() + " is now " + newStatus);
    }
}