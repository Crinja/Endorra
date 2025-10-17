package com.techtech.endorra.stockmanagement.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false) 
    private int stock;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;

    @Column
    private Double salePercentage;

    @Column(nullable = false) 
    private boolean onSale = false;


    public Product() {}

    public Product(String name, String description, BigDecimal price, int stock) 
    {
        this(name, description, price, stock, null);
    }

    public Product(String name, String description, BigDecimal price, int stock, byte[] image) 
    {
        this(name, description, price, stock, image, null, false);
    }

    public Product(String name, String description, BigDecimal price, int stock, byte[] image, Double salePercentage, boolean onSale) 
    {
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setSalePercentage(salePercentage);
        this.setOnSale(onSale);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Double getSalePercentage() { return salePercentage; }
    public void setSalePercentage(Double salePercentage) 
    {
        if (salePercentage != null && (salePercentage < 0.0 || salePercentage > 100.0)) 
        {
            throw new IllegalArgumentException("Sale percentage must be between 0 and 100");
        }

        this.salePercentage = salePercentage;
    }

    public boolean isOnSale() { return onSale; }
    public void setOnSale(boolean onSale) { this.onSale = onSale; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public void addStock(int number) { this.stock += number; }
    public void removeStock(int number) { this.stock -= number; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public BigDecimal getCurrentPrice() 
    {
        if (onSale && salePercentage != null && price != null) 
        {
            BigDecimal discount = price.multiply(BigDecimal.valueOf(salePercentage / 100));
            return price.subtract(discount).setScale(2, RoundingMode.HALF_UP);
        }

        return price;
    }

    public BigDecimal getSavings() 
    {
        if (onSale && salePercentage != null && price != null) 
        {
            BigDecimal discount = price.multiply(BigDecimal.valueOf(salePercentage / 100));
            return discount.setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }
}
