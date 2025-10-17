package com.techtech.endorra.catalog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.techtech.endorra.catalog.model.Cart;
import com.techtech.endorra.catalog.model.CartItem;
import com.techtech.endorra.catalog.repository.CartRepository;
import com.techtech.endorra.catalog.service.dto.ProductDto;
import com.techtech.endorra.catalog.service.dto.UserDto;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;

    private static final String userServiceUrl = "http://localhost:8080/user";
    private static final String stockServiceUrl = "http://localhost:8081/products";

    CartService(CartRepository cartRepository, RestTemplate restTemplate) 
    {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
    }

    private UserDto verifyUser(String token)
    {
        try 
        {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<UserDto> response = restTemplate.exchange(
                        userServiceUrl + "/me",
                        HttpMethod.GET,
                        entity,
                        UserDto.class
                );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) 
                {
                    return response.getBody();
                } 
                else 
                {
                    throw new RuntimeException("Invalid or expired token");
                }
        } 
        catch (Exception e) 
        {
        throw new RuntimeException("Invalid or expired token", e);
        }
    }

    public Cart saveCart(String token, Cart cart)
    {
        UserDto user = verifyUser(token);
        cart.setUserEmail(user.getEmail());

        return cartRepository.save(cart);
    }

    public List<Cart> getCarts(String token)
    {
        UserDto user = verifyUser(token);

        return cartRepository.findByUserEmail(user.getEmail());
    }

    public Optional<Cart> getCart(String token, Long cartId)
    {
        verifyUser(token);

        return cartRepository.findById(cartId);
    }

    public void deleteCart(String token, Long cartId)
    {
        UserDto user = verifyUser(token);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.getUserEmail().equals(user.getEmail())) {
            throw new RuntimeException("You cannot delete someone else's cart");
        }

        cartRepository.delete(cart);
    }

    public Optional<Cart> updateCart(String token, Long cartId, Cart updatedCart)
    {
        UserDto user = verifyUser(token);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.getUserEmail().equals(user.getEmail())) 
        {
            throw new RuntimeException("You cannot modify someone else's cart");
        }

        if (updatedCart.getItems() != null && !updatedCart.getItems().isEmpty()) 
        {
            for (var newItem : updatedCart.getItems()) 
            {
                if (!checkStockAvailable(newItem.getProductId(), newItem.getQuantity())) 
                {
                    throw new RuntimeException(
                        "Insufficient stock for product ID: " + newItem.getProductId()
                    );
                }

                boolean exists = false;

                for (var existingItem : cart.getItems()) 
                {
                    if (existingItem.getProductId().equals(newItem.getProductId())) 
                    {
                        existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                        exists = true;
                        break;
                    }
                }

                if (!exists) 
                {
                    cart.getItems().add(newItem);
                }
            }
        }

        Cart updated = cartRepository.save(cart);
        return Optional.of(updated);
    }

    public Optional<Cart> addItem(String token, Long cartId, ProductDto product)
    {
        UserDto user = verifyUser(token);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.getUserEmail().equals(user.getEmail())) 
        {
            throw new RuntimeException("You cannot modify someone else's cart");
        }

        Long productId = product.getId();

        if (!checkStockAvailable(productId, 1)) 
        {
            throw new RuntimeException("Insufficient stock for product ID: " + productId);
        }

        boolean exists = false;
        for (var existingItem : cart.getItems()) 
        {
            if (existingItem.getProductId().equals(productId)) {
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                exists = true;
                break;
            }
        }

        if (!exists) {
            cart.addItem(new CartItem(product.getId(), 1, product.getCurrentPrice()));
        }

        Cart updated = cartRepository.save(cart);
        return Optional.of(updated);
    }

    public Optional<Cart> removeItem(String token, Long cartId, ProductDto product)
    {
        UserDto user = verifyUser(token);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.getUserEmail().equals(user.getEmail())) 
        {
            throw new RuntimeException("You cannot modify someone else's cart");
        }

        Long productId = product.getId();

        for (var existingItem : cart.getItems()) 
        {
            if (existingItem.getProductId().equals(productId)) {
                int quantity = existingItem.getQuantity();

                if (quantity > 1)
                {
                    existingItem.setQuantity(quantity - 1);
                }
                else if (quantity == 1)
                {
                    cart.removeItem(existingItem.getProductId());
                }
                break;
            }
        }

        Cart updated = cartRepository.save(cart);
        return Optional.of(updated);
    }

    public ProductDto getProduct(Long productId)
    {
        try 
        {
            String url = stockServiceUrl + "/" + productId;

            ResponseEntity<ProductDto> product = restTemplate.getForEntity(url, ProductDto.class);

            if (product.getStatusCode().is2xxSuccessful() && product.getBody() != null) 
            {
                return product.getBody();
            }
            else 
            {
            throw new RuntimeException("Product not found or unavailable for ID: " + productId);
            }
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Error verifying stock for product ID: " + productId);
        }
    }

    private boolean checkStockAvailable(Long productId, int quantity)
    {
        ProductDto product = getProduct(productId);
        return product.getStock() >= quantity;
    }
}
