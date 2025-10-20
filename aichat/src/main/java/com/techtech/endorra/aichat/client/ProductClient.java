package com.techtech.endorra.aichat.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.techtech.endorra.aichat.service.dto.ProductDto;

@Component
public class ProductClient {
    private final RestTemplate restTemplate;

    private static final String productServiceUrl = "http://localhost:8081/products";

    ProductClient(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public ProductDto getProduct(Long id)
    {
        String url = productServiceUrl + "/" + id;

        return restTemplate.getForObject(url, ProductDto.class);
    }

    public ProductDto getProductFromName(String name)
    {
        String url = productServiceUrl + "/name/" + name.trim();

        return restTemplate.getForObject(url, ProductDto.class);
    }

    public List<ProductDto> getProductFromTags(List<String> tags) 
    {
        String tagsParam = String.join(",", tags);
        String url = productServiceUrl + "/tags?list=" + tagsParam;
        
        ProductDto[] productsArray = restTemplate.getForObject(url, ProductDto[].class);
        return Arrays.asList(productsArray);
    }
}
