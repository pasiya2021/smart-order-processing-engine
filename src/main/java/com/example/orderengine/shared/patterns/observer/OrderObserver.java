package com.example.orderengine.shared.patterns.observer;

import com.example.orderengine.domain.entity.Order;
import com.example.orderengine.domain.enums.OrderStatus;

// Every observer must implement this
public interface OrderObserver {
    void onOrderStatusChanged(Order order, OrderStatus newStatus);
}