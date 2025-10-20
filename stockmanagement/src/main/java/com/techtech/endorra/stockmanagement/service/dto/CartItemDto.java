package com.techtech.endorra.stockmanagement.service.dto;

import java.io.Serializable;

public class CartItemDto implements Serializable
    {
        private ProductDto product;
        private int quantity;

        public CartItemDto(ProductDto product, int quantity) 
        {
            this.product = product;
            this.quantity = quantity;
        }

        public ProductDto getProduct() { return product; }

        void setProduct(ProductDto product) { this.product = product; }

        public int getQuantity() { return quantity; }

        void setQuantity(int quantity) { this.quantity = quantity; }

        @Override
        public String toString() 
        {
            return "CartItemDto{" +
                    "product=" + (product != null ? product.getName() : "null") +
                    ", quantity=" + quantity +
                    '}';
        }
    }