package com.techtech.endorra.catalog.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techtech.endorra.catalog.model.Cart;
import com.techtech.endorra.catalog.service.CartService;
import com.techtech.endorra.catalog.service.dto.CartDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/cart")
public class CartController 
{
    private final CartService cartService;

    public CartController(CartService cartService) 
    {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartDto> createCart(@RequestHeader("Authorization") String token) 
    throws Exception {
        Cart saved = cartService.saveCart(token, new Cart());
        return ResponseEntity.ok(CartDto.fromEntity(saved, cartService));
    }

    @GetMapping
    public ResponseEntity<List<CartDto>> getCarts(@RequestHeader("Authorization") String token) {
        List<Cart> carts = cartService.getCarts(token);

        List<CartDto> cartDtos = carts.stream()
            .map(cart -> CartDto.fromEntity(cart, cartService))
            .toList();

        return ResponseEntity.ok(cartDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        Cart cart = cartService.getCart(token, id)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartDto cartDto = new CartDto(cart, cartService);

        return ResponseEntity.ok(cartDto);
    }
    
}
