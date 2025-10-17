package com.techtech.endorra.usermanagement.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.techtech.endorra.usermanagement.model.Role;
import com.techtech.endorra.usermanagement.model.Token;
import com.techtech.endorra.usermanagement.model.User;
import com.techtech.endorra.usermanagement.repository.UserRepository;
import com.techtech.endorra.usermanagement.service.dto.LoginResponse;
import com.techtech.endorra.usermanagement.service.dto.UserDto;

@Service
public class UserService 
{
    private final UserRepository userRepository;
    private final Map<String, Token> tokenStore = new ConcurrentHashMap<>();
    private final long TOKEN_EXPIRATION_MINUTES = 60;

    UserService(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }

    public List<User> findAll() { return userRepository.findAll(); }

    public Optional<User> findByEmail(String email) { return userRepository.findById(email); }

    public User saveUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String rawPassword) 
    {
        String hashed = hashPassword(rawPassword);
        return hashed.equals(user.getPassword());
    }

    private String hashPassword(String password) 
    {
        try 
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } 
        catch (NoSuchAlgorithmException e) 
        {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private String bytesToHex(byte[] hash) 
    {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) 
        {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void deleteUser(String token) 
    {
        Token info = tokenStore.get(token);
        
        if (info == null || LocalDateTime.now().isAfter(info.getExpiry())) 
        {
        throw new RuntimeException("Invalid or expired token");
        }

        String email = info.getEmail();

        userRepository.deleteById(email);

        tokenStore.entrySet().removeIf(entry -> entry.getValue().getEmail().equals(email));
    }

    public LoginResponse login(String email, String password) 
    {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!checkPassword(user, password)) 
        {
            throw new RuntimeException("Invalid password");
        }

        tokenStore.entrySet().removeIf(entry -> entry.getValue().getEmail().equals(email));

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES);
        tokenStore.put(token, new Token(email, expiry));

        return new LoginResponse(email, token);
    }

    public boolean isTokenValid(String token) 
    {
        Token info = tokenStore.get(token);
        if (info == null) return false;
        if (LocalDateTime.now().isAfter(info.getExpiry())) {
            tokenStore.remove(token);
            return false;
        }
        return true;
    }

    public String getEmailByToken(String token) 
    {
        Token info = tokenStore.get(token);
        return info != null ? info.getEmail() : null;
    }

    public void logout(String token) 
    {
        tokenStore.remove(token);
    }

    public boolean isUserInRole(String token, String role) 
    {
        if (!isTokenValid(token)) 
        {
            return false;
        }

        String email = getEmailByToken(token);
        Optional<User> optionalUser = findByEmail(email);

        if (optionalUser.isPresent()) 
        {
            Role userRole = optionalUser.get().getRole();
            return userRole.name().equalsIgnoreCase(role);
        } 
        else return false;
    }

    // Remove expired tokens every 60s
    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredTokens() 
    {
        tokenStore.entrySet().removeIf(entry ->
                LocalDateTime.now().isAfter(entry.getValue().getExpiry())
        );
    }

    public static UserDto toDto(User user) 
    {
    if (user == null) return null;

    UserDto userDto = new UserDto(user);

    return userDto;
    }
}
