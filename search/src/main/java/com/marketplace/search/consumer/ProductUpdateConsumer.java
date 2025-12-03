package com.marketplace.search.consumer;

import com.marketplace.search.document.ProductDocument;
import com.marketplace.search.event.ProductUpdateEvent;
import com.marketplace.search.service.IndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductUpdateConsumer {

    private final IndexService indexService;

    @KafkaListener(topics = "${kafka.topic.product-updates}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeProductUpdate(ProductUpdateEvent event) {
        log.info("Received event: {} for productId: {}", event.getEventType(), event.getProductId());

        try {
            if ("DELETE".equals(event.getEventType())) {
                indexService.deleteProduct(event.getProductId());
            } else if (event.getProduct() != null) {
                ProductDocument document = mapToDocument(event.getProduct());
                indexService.saveProduct(document);
            }
        } catch (Exception e) {
            log.error("Error processing event for productId: {}", event.getProductId(), e);
        }
    }

    private ProductDocument mapToDocument(Map<String, Object> productMap) {
        ProductDocument document = new ProductDocument();
        document.setProductId((String) productMap.get("productId"));
        document.setTitle((String) productMap.get("title"));
        document.setDescription((String) productMap.get("description"));
        
        Object priceObj = productMap.get("price");
        if (priceObj instanceof Number) {
            document.setPrice(BigDecimal.valueOf(((Number) priceObj).doubleValue()));
        }
        
        document.setImageUrl((String) productMap.get("imageUrl"));
        document.setCategory((String) productMap.get("category"));
        return document;
    }
}

