package com.example.orderengine.domain.entity;

import com.example.orderengine.domain.enums.OrderStatus;
import com.example.orderengine.domain.enums.PaymentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orders")
public class Order implements Serializable, Comparable<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String productName;
    private int quantity;
    private double totalPrice;
    private String shippingAddress;
    private int priority;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    protected Order() {}

    @Override
    public int compareTo(Order other) {
        return Integer.compare(other.priority, this.priority);
    }

    // ✅ Only change — added this method
    public void changeStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    public static class Builder {
        private String customerName;
        private String productName;
        private int quantity;
        private double totalPrice;
        private String shippingAddress;
        private int priority = 1;
        private PaymentType paymentType;

        public Builder customerName(String name)       { this.customerName = name; return this; }
        public Builder productName(String product)     { this.productName = product; return this; }
        public Builder quantity(int qty)               { this.quantity = qty; return this; }
        public Builder totalPrice(double price)        { this.totalPrice = price; return this; }
        public Builder shippingAddress(String address) { this.shippingAddress = address; return this; }
        public Builder priority(int priority)          { this.priority = priority; return this; }
        public Builder paymentType(PaymentType type)   { this.paymentType = type; return this; }

        public Order build() {
            Order order = new Order();
            order.customerName    = this.customerName;
            order.productName     = this.productName;
            order.quantity        = this.quantity;
            order.totalPrice      = this.totalPrice;
            order.shippingAddress = this.shippingAddress;
            order.priority        = this.priority;
            order.paymentType     = this.paymentType;
            order.status          = OrderStatus.PENDING;
            order.createdAt       = LocalDateTime.now();
            return order;
        }
    }
}