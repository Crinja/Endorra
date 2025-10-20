package com.techtech.endorra.aichat.service.dto;

public class AgentSupportRequest 
{
    private int sessionId;
    private String userMessage;

    // Getters and setters
    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public String getUserMessage() { return userMessage; }
    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }
}
