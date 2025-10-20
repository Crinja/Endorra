package com.techtech.endorra.aichat.controller;

import dev.langchain4j.service.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.techtech.endorra.aichat.client.UserClient;
import com.techtech.endorra.aichat.service.CustomerSupportAgent;
import com.techtech.endorra.aichat.service.dto.AgentSupportRequest;
import com.techtech.endorra.aichat.service.dto.UserDto;

@RestController
public class CustomerSupportAgentController {
    private final CustomerSupportAgent customerSupportAgent;
    private final UserClient userClient;

    public CustomerSupportAgentController(CustomerSupportAgent customerSupportAgent, UserClient userClient) 
    {
        this.customerSupportAgent = customerSupportAgent;
        this.userClient = userClient;
    }

    @PostMapping("/customerSupportAgent")
    public String customerSupportAgent(
        @RequestHeader("Authorization") String token,
        @RequestBody AgentSupportRequest request) 
    {
        UserDto userDto = userClient.getUser(token);
        String memoryKey = userDto.getEmail() + "-" + request.getSessionId();

        String userMessageWithToken = request.getUserMessage() + "\n\n[AUTH_TOKEN:" + token + "]";

        Result<String> result = customerSupportAgent.answer(memoryKey, userMessageWithToken);
        return result.content();
    }
}