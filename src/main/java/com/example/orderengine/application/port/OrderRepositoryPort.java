package com.example.orderengine.application.port;

import com.example.orderengine.domain.entity.Order;
import com.example.orderengine.domain.enums.OrderStatus;
import java.util.List;
import java.util.Optional;

// ✅ Service talks to THIS interface — not JPA directly
// This is Dependency Inversion (SOLID - D)
public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> findByStatus(OrderStatus status);
    void deleteById(Long id);
}