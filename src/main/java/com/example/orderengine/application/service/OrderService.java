package com.example.orderengine.application.service;

import com.example.orderengine.application.port.OrderRepositoryPort;
import com.example.orderengine.domain.entity.Order;
import com.example.orderengine.domain.enums.OrderStatus;
import com.example.orderengine.domain.enums.PaymentType;
import com.example.orderengine.infrastructure.messaging.NotificationMessage;
import com.example.orderengine.infrastructure.messaging.NotificationProducer;
import com.example.orderengine.shared.patterns.observer.OrderEventPublisher;
import com.example.orderengine.shared.patterns.strategy.PaymentContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepositoryPort orderRepository;  // SOLID-D: interface not impl
    private final PaymentContext paymentContext;         // Strategy pattern
    private final OrderEventPublisher eventPublisher;   // Observer pattern
    private final NotificationProducer notificationProducer;
    // ──────────────────────────────────────────────────
    // CREATE ORDER — Builder + Strategy + Observer
    // ──────────────────────────────────────────────────
    public Order createOrder(String customerName, String productName,
                             int quantity, double totalPrice,
                             String shippingAddress, int priority,
                             PaymentType paymentType) {

        log.info("Creating order for {}", customerName);

        // Builder Pattern ✅
        Order order = new Order.Builder()
                .customerName(customerName)
                .productName(productName)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .shippingAddress(shippingAddress)
                .priority(priority)
                .paymentType(paymentType)
                .build();

        // Strategy Pattern ✅ — picks right payment at runtime
        boolean paid = paymentContext.executePayment(paymentType.name(), totalPrice);
        if (!paid) throw new RuntimeException("Payment failed!");

        Order saved = orderRepository.save(order);

        // Observer Pattern ✅ — notifies all observers automatically
        eventPublisher.notifyStatusChange(saved, OrderStatus.PENDING);

        NotificationMessage message = new NotificationMessage(
                saved.getId(),
                saved.getCustomerName(),
                saved.getProductName(),
                saved.getStatus().name(),
                "EMAIL",                        // later Factory pattern picks this
                saved.getCustomerName() + "@gmail.com"  // placeholder
        );
        notificationProducer.sendNotification(message);

        return saved;
    }

    // ──────────────────────────────────────────────────
    // GET ORDER — Redis Cache ✅
    // ──────────────────────────────────────────────────
    @Cacheable(value = "orders", key = "#id")
    public Order getOrderById(Long id) {
        log.info("Fetching order #{} from DB", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    // ──────────────────────────────────────────────────
    // GET ALL ORDERS
    // ──────────────────────────────────────────────────
    @Cacheable(value = "allOrders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ──────────────────────────────────────────────────
    // UPDATE STATUS — Observer fires on every status change
    // ──────────────────────────────────────────────────
    @CacheEvict(value = "orders", key = "#id")
    public Order updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));

        // update status — Observer Pattern ✅
        eventPublisher.notifyStatusChange(order, newStatus);

        return orderRepository.save(order);
    }

    // ──────────────────────────────────────────────────
    // DELETE ORDER
    // ──────────────────────────────────────────────────
    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
        log.info("Order #{} deleted", id);
    }
}
