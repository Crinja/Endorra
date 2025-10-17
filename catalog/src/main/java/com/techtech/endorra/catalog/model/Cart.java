package com.techtech.endorra.catalog.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private boolean purchased = false;

    public Cart() {}

    public Cart(String userEmail) {
        this.setUserEmail(userEmail);
    }

    public Long getId() { return id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public boolean isPurchased() { return purchased; }

    public void addItem(CartItem item) {
        item.setCart(this);
        this.items.add(item);
    }

    public void removeItem(Long productId) {
        this.items.removeIf(i -> i.getProductId().equals(productId));
    }

    public void purchase()
    {
        this.purchased = true;
    }
}