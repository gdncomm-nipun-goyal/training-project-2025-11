package com.marketplace.search.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateEvent {

    private String eventType;
    private String productId;
    private Map<String, Object> product;
    private Long timestamp;
}

