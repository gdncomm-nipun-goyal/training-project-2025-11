package com.marketplace.product.service;

import com.marketplace.product.dto.ProductResponse;

public interface KafkaProducerService {

    void publishProductCreated(ProductResponse product);

    void publishProductUpdated(ProductResponse product);

    void publishProductDeleted(String productId);
}

