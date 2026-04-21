package com.example.orderengine.shared.patterns.strategy;

import org.springframework.stereotype.Component;

@Component
public class PayPalPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean pay(double amount) {
        System.out.println("🅿️ Processing PAYPAL payment of Rs." + amount);
        return true;
    }

    @Override
    public String getPaymentType() { return "PAYPAL"; }
}
