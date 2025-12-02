package com.marketplace.cart.service.impl;

import com.marketplace.cart.client.ProductServiceClient;
import com.marketplace.cart.client.dto.ProductResponse;
import com.marketplace.cart.dto.AddItemRequest;
import com.marketplace.cart.dto.CartItemResponse;
import com.marketplace.cart.dto.CartResponse;
import com.marketplace.cart.entity.Cart;
import com.marketplace.cart.entity.CartItem;
import com.marketplace.cart.exception.CartNotFoundException;
import com.marketplace.cart.repository.CartRepository;
import com.marketplace.cart.service.CartService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;

    @Override
    public CartResponse getCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        return mapToResponse(cart);
    }

    @Override
    public CartResponse addItem(String userId, AddItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        ProductResponse product = fetchProduct(request.getProductId());
        CartItem existingItem = findCartItem(cart, request.getProductId());

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (newQuantity <= 0) {
                cart.getItems().remove(existingItem);
            } else {
                existingItem.setQuantity(newQuantity);
                existingItem.setPrice(product.getPrice());
            }
        } else {
            if (request.getQuantity() <= 0) {
                return mapToResponse(cart);
            }
            cart.getItems().add(CartItem.builder()
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build());
        }

        calculateTotal(cart);
        log.info("Cart updated for userId: {}, productId: {}", userId, request.getProductId());
        return mapToResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse removeItem(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for userId: " + userId));

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        calculateTotal(cart);
        log.info("Item removed from cart for userId: {}, productId: {}", userId, productId);
        return mapToResponse(cartRepository.save(cart));
    }

    private Cart createNewCart(String userId) {
        return cartRepository.save(Cart.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .total(BigDecimal.ZERO)
                .build());
    }

    private CartItem findCartItem(Cart cart, String productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void calculateTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
    }

    private ProductResponse fetchProduct(String productId) {
        try {
            return productServiceClient.getProduct(productId);
        } catch (FeignException e) {
            log.error("Error fetching product: {}", productId, e);
            throw new RuntimeException("Product not found: " + productId);
        }
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toList());

        return CartResponse.builder()
                .userId(cart.getUserId())
                .items(itemResponses)
                .total(cart.getTotal())
                .build();
    }

    private CartItemResponse mapToItemResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        BeanUtils.copyProperties(item, response);
        return response;
    }
}

