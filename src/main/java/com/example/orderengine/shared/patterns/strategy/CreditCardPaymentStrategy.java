package com.example.orderengine.shared.patterns.strategy;

import org.springframework.stereotype.Component;

@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean pay(double amount) {
        System.out.println("💳 Processing CREDIT CARD payment of Rs." + amount);
        // real logic goes here (Stripe API etc.)
        return true;
    }

    @Override
    public String getPaymentType() { return "CREDIT_CARD"; }
}