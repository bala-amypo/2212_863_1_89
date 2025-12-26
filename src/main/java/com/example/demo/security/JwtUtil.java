package com.example.demo.security;

import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    
    public String generateToken(UserDetails userDetails, User user) {
        return "mock-token";
    }
    
    public boolean validateToken(String token, UserDetails userDetails) {
        return "good-token".equals(token);
    }
}