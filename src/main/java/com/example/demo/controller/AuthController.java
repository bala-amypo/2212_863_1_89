package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserServiceImpl userService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getEmail())
                .password(savedUser.getPassword())
                .authorities("ROLE_" + (savedUser.getRole() != null ? savedUser.getRole() : "USER"))
                .build();
        
        String token = jwtUtil.generateToken(userDetails, savedUser);
        
        AuthResponse response = new AuthResponse(
            token,
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        User user = userService.findByEmail(loginRequest.getEmail());
        String token = jwtUtil.generateToken(userDetails, user);
        
        AuthResponse response = new AuthResponse(
            token,
            user.getId(),
            user.getEmail(),
            user.getRole()
        );
        
        return ResponseEntity.ok(response);
    }
}