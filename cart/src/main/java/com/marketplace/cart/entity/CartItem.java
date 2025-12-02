package com.marketplace.cart.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Field("product_id")
    private String productId;

    @Field("quantity")
    private Integer quantity;

    @Field("price")
    private BigDecimal price;
}

