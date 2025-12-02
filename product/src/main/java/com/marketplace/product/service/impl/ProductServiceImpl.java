package com.marketplace.product.service.impl;

import com.marketplace.product.dto.ProductRequest;
import com.marketplace.product.dto.ProductResponse;
import com.marketplace.product.entity.Product;
import com.marketplace.product.exception.ProductNotFoundException;
import com.marketplace.product.repository.ProductRepository;
import com.marketplace.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        String productId = generateUniqueProductId();
        
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        product.setProductId(productId);

        Product savedProduct = productRepository.save(product);
        log.info("Product created with productId: {}", savedProduct.getProductId());

        return mapToResponse(savedProduct);
    }

    private String generateUniqueProductId() {
        long timestamp = System.currentTimeMillis();
        return "MTA-" + timestamp;
    }

    @Override
    public ProductResponse getProduct(String productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getProducts(List<String> productIds) {
        List<Product> products = productRepository.findByProductIdIn(productIds);
        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(String productId, ProductRequest request) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        BeanUtils.copyProperties(request, product, "productId");
        Product updatedProduct = productRepository.save(product);
        log.info("Product updated with productId: {}", updatedProduct.getProductId());

        return mapToResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        
        productRepository.delete(product);
        log.info("Product deleted with productId: {}", productId);
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        BeanUtils.copyProperties(product, response);
        return response;
    }
}

