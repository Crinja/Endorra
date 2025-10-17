package com.techtech.endorra.usermanagement.service.dto;

import com.techtech.endorra.usermanagement.model.Role;
import com.techtech.endorra.usermanagement.model.User;

public class UserDto 
{

    private String email;
    private Role role;
    private String address;
    private String mobile;

    public UserDto() {}

    public UserDto(User user) 
    {
        this(user.getEmail(), user.getRole(), user.getAddress(), user.getMobile());
    }

    public UserDto(String email, Role role, String address, String mobile) {
        this.setEmail(email);
        this.setRole(role);
        this.setAddress(address);
        this.setMobile(mobile);
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public static UserDto fromEntity(User user) { return new UserDto(user); }
}