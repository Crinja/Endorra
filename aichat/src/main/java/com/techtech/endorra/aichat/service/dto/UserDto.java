package com.techtech.endorra.aichat.service.dto;

public class UserDto 
{

    private String email;
    private String address;
    private String mobile;

    public UserDto(String email, String address, String mobile) {
        this.setEmail(email);
        this.setAddress(address);
        this.setMobile(mobile);
    }

    public String getEmail() { return email; }
    void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    void setAddress(String address) { this.address = address; }

    public String getMobile() { return mobile; }
    void setMobile(String mobile) { this.mobile = mobile; }
}