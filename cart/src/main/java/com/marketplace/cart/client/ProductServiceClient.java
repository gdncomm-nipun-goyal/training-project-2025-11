package com.marketplace.cart.client;

import com.marketplace.cart.client.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "${feign.client.config.product-service.url}")
public interface ProductServiceClient {

    @GetMapping("/api/products")
    ProductResponse getProduct(@RequestParam("productId") String productId);
}

