package com.techtech.endorra.stockmanagement.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techtech.endorra.stockmanagement.model.Product;
import com.techtech.endorra.stockmanagement.service.ProductService;
import com.techtech.endorra.stockmanagement.service.dto.ProductDto;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) 
    {
        this.productService = productService;
    }


    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestHeader("Authorization") String token,
            @RequestBody Product newProduct
    ) 
    throws Exception {
        Product saved = productService.saveProduct(token, newProduct);
        return ResponseEntity.ok(ProductDto.fromEntity(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) 
    {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(ProductDto.fromEntity(product));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductDto> getProductFromName(@PathVariable String name) 
    {
        String trimmedName = name.trim();
        Product product = productService.findByNameIgnoreCase(trimmedName)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(ProductDto.fromEntity(product));
    }

    @GetMapping("/tags")
    public ResponseEntity<List<ProductDto>> getProductsByTags(
            @RequestParam("list") String tagList) 
    {
        List<String> tags = Arrays.stream(tagList.split(","))
                                .map(String::trim)
                                .filter(t -> !t.isEmpty())
                                .toList();

        List<ProductDto> products = productService.findByTag(tags);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
        @RequestHeader("Authorization") String token,
        @PathVariable Long id,
        @RequestBody Product product) 
    {
        product.setId(id);
        Product updated = productService.updateProduct(token, product);
        return ResponseEntity.ok(ProductDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@RequestHeader("Authorization") String token, @PathVariable Long id) 
    {
        productService.deleteProduct(token, id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PostMapping("/{id}/set-sale")
    public ResponseEntity<String> setSale(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestParam double salePercentage) 
    {
        productService.setSale(token, id, salePercentage);
        return ResponseEntity.ok("Sale applied successfully");
    }

    @PostMapping("/{id}/remove-sale")
    public ResponseEntity<String> removeSale(@RequestHeader("Authorization") String token, @PathVariable Long id) 
    {
        productService.removeSale(token, id);
        return ResponseEntity.ok("Sale removed successfully");
    }
}
