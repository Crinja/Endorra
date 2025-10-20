package com.techtech.endorra.aichat.agentic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.techtech.endorra.aichat.client.CartClient;
import com.techtech.endorra.aichat.client.ProductClient;
import com.techtech.endorra.aichat.client.UserClient;
import com.techtech.endorra.aichat.service.dto.CartDto;
import com.techtech.endorra.aichat.service.dto.ProductDto;
import com.techtech.endorra.aichat.service.dto.UserDto;

import dev.langchain4j.agent.tool.Tool;

@Component
public class Tools {
    private final UserClient userClient;
    private final ProductClient productClient;
    private final CartClient cartClient;

    public Tools(UserClient userClient, ProductClient productClient, CartClient cartClient)
    {
        this.userClient = userClient;
        this.productClient = productClient;
        this.cartClient = cartClient;
    }

    @Tool public UserDto getUserInformation(String token) { return userClient.getUser(token); }

    @Tool public ProductDto getProductInformation(Long id) { return productClient.getProduct(id); }
    @Tool public ProductDto getProductInformationFromName(String name) { return productClient.getProductFromName(name); }
    @Tool public List<ProductDto> getProductInformationFromTags(List<String> tags) { return productClient.getProductFromTags(tags); }

    @Tool public CartDto getCart(String token, Long cartId) { return cartClient.getCart(token, cartId); }
    @Tool public List<CartDto> getAllCarts(String token) { return cartClient.getAllCarts(token); }
    @Tool public CartDto createCart(String token) { return cartClient.createCart(token); }
}
