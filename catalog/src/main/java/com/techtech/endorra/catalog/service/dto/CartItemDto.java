package com.techtech.endorra.catalog.service.dto;

import java.io.Serializable;

public class CartItemDto
{
    private ProductDto product;
    private int quantity;

    public CartItemDto(ProductDto product, int quantity) 
    {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductDto getProduct() { return product; }

    public void setProduct(ProductDto product) { this.product = product; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() 
    {
        return "CartItemDto{" +
                "product=" + (product != null ? product.getName() : "null") +
                ", quantity=" + quantity +
                '}';
    }
}
