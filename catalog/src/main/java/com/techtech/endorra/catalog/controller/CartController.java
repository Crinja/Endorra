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
import com.techtech.endorra.catalog.service.dto.ProductDto;

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
    throws Exception 
    {
        Cart saved = cartService.saveCart(token, new Cart());
        return ResponseEntity.ok(CartDto.fromEntity(saved, cartService));
    }

    @GetMapping
    public ResponseEntity<List<CartDto>> getCarts(@RequestHeader("Authorization") String token) 
    {
        List<Cart> carts = cartService.getCarts(token);

        List<CartDto> cartDtos = carts.stream()
            .map(cart -> CartDto.fromEntity(cart, cartService))
            .toList();

        return ResponseEntity.ok(cartDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) 
    {
        Cart cart = cartService.getCart(token, id)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartDto cartDto = new CartDto(cart, cartService);

        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> deleteCart(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) 
    {
        cartService.deleteCart(token, id);

        return ResponseEntity.ok("Cart deleted");
    }

    @PostMapping("/{id}/add/{productId}")
    public ResponseEntity<CartDto> addToCart(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, @PathVariable("productId") Long productId) 
    {
        ProductDto productDto = cartService.getProduct(productId);

        Cart cart = cartService.addItem(token, id, productDto)
            .orElseThrow(() -> new RuntimeException("Unable to add item to cart"));

        CartDto cartDto = new CartDto(cart, cartService);

        return ResponseEntity.ok(cartDto);
    }
    
    @PostMapping("/{id}/remove/{productId}")
    public ResponseEntity<CartDto> removeFromCart(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, @PathVariable("productId") Long productId) 
    {
        ProductDto productDto = cartService.getProduct(productId);

        Cart cart = cartService.removeItem(token, id, productDto)
            .orElseThrow(() -> new RuntimeException("Unable to removce item from cart"));

        CartDto cartDto = new CartDto(cart, cartService);

        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<CartDto> purchase(@RequestHeader("Authorization") String token, @PathVariable("id") Long id)
    {
        Cart cart = cartService.purchase(token, id)
            .orElseThrow(() -> new RuntimeException("Unable to get cart"));

        return ResponseEntity.ok(new CartDto(cart, cartService));
    }
}
