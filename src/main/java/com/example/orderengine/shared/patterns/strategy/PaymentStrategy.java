package com.example.orderengine.shared.patterns.strategy;

// Contract — every payment method must follow this
public interface PaymentStrategy {
    boolean pay(double amount);
    String getPaymentType();
}
