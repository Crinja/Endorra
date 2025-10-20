package com.techtech.endorra.aichat.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.techtech.endorra.aichat.service.dto.UserDto;

@Component
public class UserClient {
    private final RestTemplate restTemplate;

    private static final String userServiceUrl = "http://localhost:8080/user";

    UserClient(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public UserDto getUser(String token)
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
}
