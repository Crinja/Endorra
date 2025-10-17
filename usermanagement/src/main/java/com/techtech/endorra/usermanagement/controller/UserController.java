package com.techtech.endorra.usermanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techtech.endorra.usermanagement.model.Role;
import com.techtech.endorra.usermanagement.model.User;
import com.techtech.endorra.usermanagement.service.UserService;
import com.techtech.endorra.usermanagement.service.dto.LoginRequest;
import com.techtech.endorra.usermanagement.service.dto.LoginResponse;
import com.techtech.endorra.usermanagement.service.dto.UserDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/user")
public class UserController
{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody User newUser) {
        if (userService.findByEmail(newUser.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists.");
        }

        User user = userService.saveUser(newUser);
        UserDto dto = UserDto.fromEntity(user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String token) {
        userService.deleteUser(token);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        String email = userService.getEmailByToken(token);
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto dto = UserDto.fromEntity(user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/verify-role")
    public ResponseEntity<String> verifyRole(
            @RequestHeader("Authorization") String token,
            @RequestParam String role) {

        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        try {
            Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Role does not exist: " + role);
        }

        boolean hasRole = userService.isUserInRole(token, role);
        if (hasRole) {
            return ResponseEntity.ok("User has role: " + role);
        } else {
            return ResponseEntity.status(403).body("User does not have required role");
        }
    }
}