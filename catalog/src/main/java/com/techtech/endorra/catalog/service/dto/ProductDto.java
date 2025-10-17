package com.techtech.endorra.catalog.service.dto;

import java.math.BigDecimal;

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

    public ProductDto(Long id, String name, String description, BigDecimal price, int stock,
                      boolean onSale, Double salePercentage,
                      BigDecimal currentPrice, BigDecimal savings,
                      String imageBase64) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setPrice(currentPrice);
        this.setOnSale(onSale);
        this.setSalePercentage(salePercentage);
        this.setCurrentPrice(currentPrice);
        this.setSavings(savings);
        this.setImageBase64(imageBase64);
    }

    public Long getId() { return id; }
    void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    void setPrice(BigDecimal price) { this.price = price; }

    public int getStock() { return stock; }
    void setStock(int stock) { this.stock = stock; }

    public boolean isOnSale() { return onSale; }
    void setOnSale(boolean onSale) { this.onSale = onSale; }

    public Double getSalePercentage() { return salePercentage; }
    void setSalePercentage(Double salePercentage) { this.salePercentage = salePercentage; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public BigDecimal getSavings() { return savings; }
    void setSavings(BigDecimal savings) { this.savings = savings; }

    public String getImageBase64() { return imageBase64; }
    void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}