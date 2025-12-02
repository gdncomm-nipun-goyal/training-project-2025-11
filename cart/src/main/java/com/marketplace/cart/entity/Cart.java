package com.marketplace.cart.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("user_id")
    private String userId;

    @Field("items")
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @Field("total")
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;
}

