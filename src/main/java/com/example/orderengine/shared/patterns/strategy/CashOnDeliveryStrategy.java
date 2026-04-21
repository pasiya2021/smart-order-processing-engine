package com.example.orderengine.shared.patterns.strategy;

import org.springframework.stereotype.Component;

@Component
public class CashOnDeliveryStrategy implements PaymentStrategy {

    @Override
    public boolean pay(double amount) {
        System.out.println("💵 COD order confirmed. Pay Rs." + amount + " on delivery.");
        return true;
    }

    @Override
    public String getPaymentType() { return "CASH_ON_DELIVERY"; }
}