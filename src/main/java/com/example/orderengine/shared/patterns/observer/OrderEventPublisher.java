package com.example.orderengine.shared.patterns.observer;

import com.example.orderengine.domain.entity.Order;
import com.example.orderengine.domain.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

// ✅ Holds all observers and notifies them all at once
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    // Spring auto-injects ALL OrderObserver implementations
    private final List<OrderObserver> observers;

    public void notifyStatusChange(Order order, OrderStatus newStatus) {
        System.out.println("🔔 Notifying " + observers.size() + " observers...");
        observers.forEach(o -> o.onOrderStatusChanged(order, newStatus));
    }
}