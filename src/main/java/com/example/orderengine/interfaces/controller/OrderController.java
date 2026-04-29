package com.example.orderengine.interfaces.controller;

import com.example.orderengine.application.service.OrderService;
import com.example.orderengine.domain.entity.Order;
import com.example.orderengine.interfaces.controller.dto.CreateOrderRequest;
import com.example.orderengine.interfaces.controller.dto.UpdateStatusRequest;
import com.example.orderengine.shared.patterns.OrderPriorityQueue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderPriorityQueue orderPriorityQueue;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("POST /api/orders — {}", request.getCustomerName());

        Order order = orderService.createOrder(
                request.getCustomerName(),
                request.getProductName(),
                request.getQuantity(),
                request.getTotalPrice(),
                request.getShippingAddress(),
                request.getPriority(),
                request.getPaymentType()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }


    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {

        Order updated = orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/orders/queue/next — get highest priority order
    @GetMapping("/queue/next")
    public ResponseEntity<Order> getNextOrder() {
        Order next = orderPriorityQueue.dequeue();
        if (next == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(next);
    }

    // GET /api/orders/queue/size — how many in queue
    @GetMapping("/queue/size")
    public ResponseEntity<Integer> getQueueSize() {
        return ResponseEntity.ok(orderPriorityQueue.size());
    }
}