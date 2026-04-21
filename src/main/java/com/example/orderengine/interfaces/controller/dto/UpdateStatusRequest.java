package com.example.orderengine.interfaces.controller.dto;

import com.example.orderengine.domain.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {

    @NotNull(message = "Status is required")
    private OrderStatus status;
}