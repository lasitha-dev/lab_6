package com.sliit.lab6.orderservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderCreatedEvent(
        @NotBlank(message = "orderId is required")
        String orderId,
        @NotBlank(message = "item is required")
        String item,
        @Min(value = 1, message = "quantity must be greater than zero")
        int quantity
) {
}