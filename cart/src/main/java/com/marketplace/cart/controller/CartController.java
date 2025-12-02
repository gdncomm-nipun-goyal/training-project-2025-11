package com.marketplace.cart.controller;

import com.marketplace.cart.dto.AddItemRequest;
import com.marketplace.cart.dto.CartResponse;
import com.marketplace.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestHeader("X-User-Id") String userId) {
        CartResponse response = cartService.getCart(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody AddItemRequest request) {
        CartResponse response = cartService.addItem(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items")
    public ResponseEntity<CartResponse> removeItem(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam("productId") String productId) {
        CartResponse response = cartService.removeItem(userId, productId);
        return ResponseEntity.ok(response);
    }
}

