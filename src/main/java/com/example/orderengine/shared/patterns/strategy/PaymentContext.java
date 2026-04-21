package com.example.orderengine.shared.patterns.strategy;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// ✅ Picks the right strategy based on payment type string
@Component
public class PaymentContext {

    private final Map<String, PaymentStrategy> strategies;

    // Spring auto-injects ALL PaymentStrategy implementations
    public PaymentContext(List<PaymentStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(PaymentStrategy::getPaymentType, s -> s));
    }

    public boolean executePayment(String paymentType, double amount) {
        PaymentStrategy strategy = strategies.get(paymentType);
        if (strategy == null) throw new IllegalArgumentException("Unknown payment type: " + paymentType);
        return strategy.pay(amount);
    }
}