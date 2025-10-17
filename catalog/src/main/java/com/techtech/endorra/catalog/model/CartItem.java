package com.techtech.endorra.catalog.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int quantity;

    private BigDecimal lastRefreshedPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartItem() {}

    public CartItem(Long productId, int quantity, BigDecimal lastRefreshedPrice) {
        this.setProductId(productId);
        this.setQuantity(quantity);
        this.setLastRefreshedPrice(lastRefreshedPrice);
    }

    public Long getId() { return id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getLastRefreshedPrice() { return lastRefreshedPrice; }
    public void setLastRefreshedPrice(BigDecimal lastRefreshedPrice) { this.lastRefreshedPrice = lastRefreshedPrice; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
}
