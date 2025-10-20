package com.techtech.endorra.aichat.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.techtech.endorra.aichat.service.dto.CartDto;

@Component
public class CartClient {
    private final RestTemplate restTemplate;

    private static final String cartServiceUrl = "http://localhost:8082/cart";

    CartClient(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public CartDto createCart(String token)
    {
        try 
        {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<CartDto> response = restTemplate.exchange(
                        cartServiceUrl,
                        HttpMethod.POST,
                        entity,
                        CartDto.class
                );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) 
                {
                    return response.getBody();
                } 
                else 
                {
                    String errorMessage = "Error creating cart: "
                        + response.getStatusCode()
                        + (response.hasBody() ? " - " + response.getBody() : "");
                    throw new RuntimeException(errorMessage);
                }
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Unexpected error while creating cart", e);
        }
    }

    public List<CartDto> getAllCarts(String token)
    {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<CartDto[]> response = restTemplate.exchange(
                    cartServiceUrl,
                    HttpMethod.GET,
                    entity,
                    CartDto[].class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) 
            {
                return Arrays.asList(response.getBody());
            } 
            else 
            {
                String errorMessage = "Error getting cart: "
                        + response.getStatusCode()
                        + (response.hasBody() ? " - " + response.getBody() : "");
                throw new RuntimeException(errorMessage);
            }
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Unexpected error while getting cart", e);
        }
    }

    public CartDto getCart(String token, Long id)
    {
        try 
        {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<CartDto> response = restTemplate.exchange(
                        cartServiceUrl + "/" + id,
                        HttpMethod.GET,
                        entity,
                        CartDto.class
                );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) 
                {
                    return response.getBody();
                } 
                else 
                {
                    String errorMessage = "Error getting cart: "
                        + response.getStatusCode()
                        + (response.hasBody() ? " - " + response.getBody() : "");
                    throw new RuntimeException(errorMessage);
                }
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Unexpected error while getting cart", e);
        }
    }
}
