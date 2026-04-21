package com.example.orderengine.interfaces.controller.dto;

import com.example.orderengine.domain.enums.PaymentType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Product name is required")
    private String productName;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Positive(message = "Price must be positive")
    private double totalPrice;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @Min(1) @Max(5)
    private int priority = 1;  // default priority

    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;
}