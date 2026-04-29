package com.example.orderengine.shared.patterns;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RateLimiter {

    // ✅ DSA — Sliding Window using Deque + HashMap
    // Map<customerName, Deque<timestamps>>
    private final Map<String, Deque<Long>> requestMap = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS = 5;        // max 5 orders
    private static final long WINDOW_MS = 60_000L;    // per 60 seconds

    public boolean isAllowed(String customerName) {
        long now = System.currentTimeMillis();

        // get or create deque for this customer
        requestMap.putIfAbsent(customerName, new ArrayDeque<>());
        Deque<Long> timestamps = requestMap.get(customerName);

        // ✅ Sliding Window — remove timestamps older than 1 minute
        while (!timestamps.isEmpty() && now - timestamps.peekFirst() > WINDOW_MS) {
            timestamps.pollFirst();
            log.info("🗑️ Removed old timestamp for {}", customerName);
        }

        log.info("📊 {} has made {} requests in last 60s",
                customerName, timestamps.size());

        // check if under limit
        if (timestamps.size() < MAX_REQUESTS) {
            timestamps.addLast(now);   // record this request
            return true;               // ✅ allowed
        }

        log.warn("🚫 Rate limit hit for {}", customerName);
        return false;                  // ❌ blocked
    }
}