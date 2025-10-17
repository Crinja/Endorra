package com.techtech.endorra.stockmanagement.service.dto;

import java.math.BigDecimal;

import com.techtech.endorra.stockmanagement.model.Product;

public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private boolean onSale;
    private Double salePercentage;
    private BigDecimal currentPrice;
    private BigDecimal savings;
    private String imageBase64;
    

    public ProductDto() {}

    public ProductDto(Product product) 
    {
        this(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.isOnSale(),
            product.getSalePercentage(),
            product.getCurrentPrice(),
            product.getSavings(),
            product.getImage()
        );
    }

    public ProductDto(Long id, String name, String description, BigDecimal price, int stock,
                      boolean onSale, Double salePercentage,
                      BigDecimal currentPrice, BigDecimal savings,
                      byte[] image) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setPrice(currentPrice);
        this.setOnSale(onSale);
        this.setSalePercentage(salePercentage);
        this.setCurrentPrice(currentPrice);
        this.setSavings(savings);
        this.setImageBase64(image);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isOnSale() { return onSale; }
    public void setOnSale(boolean onSale) { this.onSale = onSale; }

    public Double getSalePercentage() { return salePercentage; }
    public void setSalePercentage(Double salePercentage) { this.salePercentage = salePercentage; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public BigDecimal getSavings() { return savings; }
    public void setSavings(BigDecimal savings) { this.savings = savings; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(byte[] image) { 
        String imageBase64 = null;
        if (image != null) {
            imageBase64 = java.util.Base64.getEncoder().encodeToString(image);
        }

        this.imageBase64 = imageBase64;
    }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public static ProductDto fromEntity(Product product) { return new ProductDto(product); }
}