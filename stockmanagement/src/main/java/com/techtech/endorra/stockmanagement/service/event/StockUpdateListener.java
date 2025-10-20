package com.techtech.endorra.stockmanagement.service.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.techtech.endorra.stockmanagement.service.ProductService;

@Service
public class StockUpdateListener {

    private final ProductService productService;

    public StockUpdateListener(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(
        topics = "cart-purchased",
        groupId = "stock-service",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleCartPurchasedEvent(CartPurchasedEventDto event) {
        for (CartPurchasedEventDto.CartItemEvent item : event.getItems()) {
            productService.removeStock(item.getProductId(), item.getQuantity());
        }
    }
}
