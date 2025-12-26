package com.example.demo.controller;

import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;
import com.example.demo.service.impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "Users-Controller", description = "Operations related to Users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Operation(summary = "Register user")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody User user) {

        User savedUser = userService.registerUser(user);

        return ResponseEntity.ok(
            new UserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getRole())
        );
    }

    @Operation(summary = "List all users")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(u -> new UserResponse(u.getId(), u.getEmail(), u.getRole()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok(
            new UserResponse(user.getId(), user.getEmail(), user.getRole())
        );
    }
}
