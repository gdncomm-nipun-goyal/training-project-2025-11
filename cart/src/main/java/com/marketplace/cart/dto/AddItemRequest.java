package com.marketplace.cart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemRequest {

    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;  // Can be positive (add) or negative (reduce)
}

