package com.techtech.endorra.aichat.service.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDto 
{

    private Long id;
    private String userEmail;
    private List<CartItemDto> items;
    private BigDecimal total;

    public CartDto(Long id, String userEmail, List<CartItemDto> items) 
    {
        this.setId(id);
        this.setUserEmail(userEmail);
        this.items = items != null ? items : new ArrayList<>();
        recalculateTotal();
    }

    public Long getId() { return id; }
    void setId(Long id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public List<CartItemDto> getItems() { return items; }
    void setItems(List<CartItemDto> items) { this.items = items; }

    public BigDecimal getTotal() { return total; }
    void setTotal(BigDecimal total) { this.total = total; }


    public void recalculateTotal() {
        if (this.items == null) return;

        this.total = items.stream()
                .map(item -> item.getProduct().getCurrentPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static class CartItemDto 
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

    @Override
    public String toString() 
    {
        return "CartDto{" +
                "id=" + id +
                ", userEmail=" + userEmail +
                ", items=" + items +
                ", total=" + total +
                '}';
    }
}
