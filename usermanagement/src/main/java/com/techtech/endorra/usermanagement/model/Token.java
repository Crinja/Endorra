package com.techtech.endorra.usermanagement.model;

import java.time.LocalDateTime;

public class Token 
{
    private String email;
    private LocalDateTime expiry;

    public Token(String email, LocalDateTime expiry) 
    {
        this.email = email;
        this.expiry = expiry;
    }

    public String getEmail() { return email; }
    public LocalDateTime getExpiry() { return expiry; }
    public void setExpiry(LocalDateTime expiry) { this.expiry = expiry; }
}
