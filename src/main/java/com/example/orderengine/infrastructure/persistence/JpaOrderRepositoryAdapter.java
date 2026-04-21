package com.example.orderengine.infrastructure.persistence;

import com.example.orderengine.application.port.OrderRepositoryPort;
import com.example.orderengine.domain.entity.Order;
import com.example.orderengine.domain.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaOrderRepositoryAdapter implements OrderRepositoryPort {

    // ✅ Uses Spring JPA under the hood — but service never knows this
    private final SpringDataOrderRepository jpaRepository;

    @Override
    public Order save(Order order) {
        return jpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return jpaRepository.findByStatus(status);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}


//
//### Why This Structure?
//        ```
//OrderService  →  OrderRepositoryPort (interface)
//                        ↑
//JpaOrderRepositoryAdapter (real impl)
//```
//
//Tomorrow if you want to swap H2 → PostgreSQL or even MongoDB, you **only change the adapter** — the service doesn't touch at all. That's the power of SOLID-D.
//
//---
//
//        ## ✅ Step 2 Checklist
//```
//        □ OrderRepositoryPort interface created
//        □ NotificationSenderPort interface created
//        □ SpringDataOrderRepository (JPA) created
//        □ JpaOrderRepositoryAdapter created
//        □ App still starts with no errors (mvn spring-boot:run)