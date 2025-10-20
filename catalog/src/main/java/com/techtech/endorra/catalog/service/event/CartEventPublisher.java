package com.techtech.endorra.catalog.service.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartEventPublisher {
    private final KafkaTemplate<String, CartPurchasedEventDto> kafkaTemplate;

    public CartEventPublisher(KafkaTemplate<String, CartPurchasedEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCartPurchased(CartPurchasedEventDto event) {
        kafkaTemplate.send("cart-purchased", event);
    }
}
