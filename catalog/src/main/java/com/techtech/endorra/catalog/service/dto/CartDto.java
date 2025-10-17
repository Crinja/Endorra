package com.techtech.endorra.catalog.service.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techtech.endorra.catalog.model.Cart;
import com.techtech.endorra.catalog.model.CartItem;
import com.techtech.endorra.catalog.service.CartService;

public class CartDto 
{

    private Long id;
    private String userEmail;
    private List<CartItemDto> items;
    private BigDecimal total;

    public CartDto(Cart cart, CartService cartService)
    {
        this.setId(cart.getId());
        this.setUserEmail(cart.getUserEmail());

        for (CartItem item : cart.getItems())
        {
            ProductDto product = cartService.getProduct(item.getProductId());
            this.items.add(new CartItemDto(product, item.getQuantity()));
        }
        
        recalculateTotal();
    }

    public CartDto(Long id, String userEmail, List<CartItemDto> items) 
    {
        this.setId(id);
        this.setUserEmail(userEmail);
        this.items = items != null ? items : new ArrayList<>();
        recalculateTotal();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }


    public void recalculateTotal() {
        if (this.items == null) return;

        this.total = items.stream()
                .map(item -> item.getProduct().getCurrentPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(ProductDto product, int quantity) 
    {
        if (items == null) 
        {
            items = new ArrayList<>();
        }
        items.add(new CartItemDto(product, quantity));
        recalculateTotal();
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

    public static CartDto fromEntity(Cart cart, CartService cartService) { return new CartDto(cart, cartService); }
}
