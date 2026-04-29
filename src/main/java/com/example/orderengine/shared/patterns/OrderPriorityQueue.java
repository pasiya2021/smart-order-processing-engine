package com.example.orderengine.shared.patterns;

import com.example.orderengine.domain.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.PriorityQueue;

@Slf4j
@Component
public class OrderPriorityQueue {

    // ✅ DSA — PriorityQueue sorts by Order.compareTo()
    // Higher priority number = processed first
    private final PriorityQueue<Order> queue = new PriorityQueue<>();

    // Add order to queue
    public void enqueue(Order order) {
        queue.offer(order);
        log.info("📥 Queued → Order #{} | Priority: {} | Product: {}",
                order.getId(), order.getPriority(), order.getProductName());
    }

    // Get highest priority order
    public Order dequeue() {
        Order order = queue.poll();
        if (order != null) {
            log.info("📤 Processing → Order #{} | Priority: {}",
                    order.getId(), order.getPriority());
        }
        return order;
    }

    // Peek without removing
    public Order peek() {
        return queue.peek();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    // Show all orders in priority order
    public void printQueue() {
        PriorityQueue<Order> temp = new PriorityQueue<>(queue);
        log.info("📋 Current Queue ({} orders):", queue.size());
        while (!temp.isEmpty()) {
            Order o = temp.poll();
            log.info("   → Order #{} | Priority: {} | {}",
                    o.getId(), o.getPriority(), o.getProductName());
        }
    }
}