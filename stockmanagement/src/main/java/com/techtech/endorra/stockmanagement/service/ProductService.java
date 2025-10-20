package com.techtech.endorra.stockmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.techtech.endorra.stockmanagement.model.Product;
import com.techtech.endorra.stockmanagement.repository.ProductRepository;
import com.techtech.endorra.stockmanagement.service.dto.ProductDto;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    private final String userServiceUrl = "http://localhost:8080/user";

    ProductService(ProductRepository productRepository, RestTemplate restTemplate) 
    {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }

    private void checkAdmin(String token) {
    try 
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                userServiceUrl + "/verify-role?role=ADMIN",
                HttpMethod.GET,
                entity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) 
        {
            throw new RuntimeException("User is not admin");
        }
    } 
    catch (Exception e) 
    {
        throw new RuntimeException("User is not admin");
    }
}

    public Product saveProduct(String token, Product product)
    {
        checkAdmin(token);
        return productRepository.save(product);
    }

    public List<Product> findAll() { return productRepository.findAll(); }

    public Optional<Product> findById(Long id) { return productRepository.findById(id); }

    public Optional<Product> findByNameIgnoreCase(String name) { return productRepository.findByNameIgnoreCase(name); }

    public List<ProductDto> findByTag(List<String> tags) 
    {
        List<String> lowered = tags.stream()
            .map(String::toLowerCase)
            .toList();

        return productRepository.findByAnyTags(lowered)
                .stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Product updateProduct(String token, Product product) 
    {
        checkAdmin(token);

        if (product.getId() == null || !productRepository.existsById(product.getId())) {
            throw new RuntimeException("Product not found");
        }
        return productRepository.save(product);
    }

    public void deleteProduct(String token, Long id) 
    { 
        checkAdmin(token);
        productRepository.deleteById(id); 
    }

    public void addStock(Long productId, int amount) 
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.addStock(amount);
        productRepository.save(product);
    }

    public void removeStock(Long productId, int amount) 
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getStock() < amount) 
        {
            throw new RuntimeException("Not enough stock");
        }
        product.removeStock(amount);
        productRepository.save(product);
    }

    public void setSale(String token, Long productId, double salePercentage) 
    {
        checkAdmin(token);

        if (salePercentage < 0 || salePercentage > 100) 
        {
            throw new IllegalArgumentException("Sale percentage must be between 0 and 100");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setSalePercentage(salePercentage);
        product.setOnSale(true);
        productRepository.save(product);
    }

    public void removeSale(String token, Long productId) 
    {
        checkAdmin(token);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setSalePercentage(null);
        product.setOnSale(false);
        productRepository.save(product);
    }
}
