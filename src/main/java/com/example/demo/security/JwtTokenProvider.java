package com.example.demo.security;

import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    
    public String generateToken(UserDetails userDetails, User user) {
        return "jwt-token-" + user.getId();
    }
    
    public boolean validateToken(String token, UserDetails userDetails) {
        return token != null && token.startsWith("jwt-token-");
    }
}