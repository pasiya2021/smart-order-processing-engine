package com.example.orderengine.shared.patterns.observer;

import com.example.orderengine.domain.entity.Order;
import com.example.orderengine.domain.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class InventoryObserver implements OrderObserver {

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus newStatus) {
        if (newStatus == OrderStatus.CONFIRMED) {
            System.out.println("📦 INVENTORY updated → Reserved "
                    + order.getQuantity() + "x " + order.getProductName());
        }
    }
}