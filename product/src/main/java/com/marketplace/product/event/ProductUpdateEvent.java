package com.marketplace.product.event;

import com.marketplace.product.dto.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateEvent {

    private String eventType;
    private String productId;
    private ProductResponse product;
    private Long timestamp;
}

