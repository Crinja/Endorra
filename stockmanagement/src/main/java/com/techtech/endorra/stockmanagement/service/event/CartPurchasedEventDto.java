package com.techtech.endorra.stockmanagement.service.event;

import java.io.Serializable;
import java.util.List;

public class CartPurchasedEventDto implements Serializable
{
    private List<CartItemEvent> items;

    public CartPurchasedEventDto() {}

    public CartPurchasedEventDto(List<CartItemEvent> items) { this.items = items; }

    public List<CartItemEvent> getItems() { return items; }

    public void setItems(List<CartItemEvent> items) { this.items = items; }

    public static class CartItemEvent implements Serializable {
        private Long productId;
        private int quantity;

        public CartItemEvent() {}

        public CartItemEvent(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
