package com.marketplace.product.controller;

import com.marketplace.product.dto.ProductIdsRequest;
import com.marketplace.product.dto.ProductRequest;
import com.marketplace.product.dto.ProductResponse;
import com.marketplace.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getProduct(@RequestParam("productId") String productId) {
        ProductResponse response = productService.getProduct(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductResponse>> getProducts(@Valid @RequestBody ProductIdsRequest request) {
        List<ProductResponse> responses = productService.getProducts(request.getIds());
        return ResponseEntity.ok(responses);
    }

    @PutMapping
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestParam("productId") String productId,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateProduct(productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@RequestParam("productId") String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}

