package com.example.orderengine.infrastructure.persistence;

import com.example.orderengine.domain.entity.Order;
import com.example.orderengine.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Spring Data JPA magic — no implementation needed
public interface SpringDataOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
}