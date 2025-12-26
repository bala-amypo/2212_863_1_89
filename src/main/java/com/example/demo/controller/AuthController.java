@PostMapping("/register")
public ResponseEntity<AuthResponse> register(@RequestBody User user) {

    User savedUser = userService.registerUser(user);

    String token = jwtUtil.generateToken(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole()
    );

    return ResponseEntity.ok(
            new AuthResponse(
                    token,
                    savedUser.getId(),
                    savedUser.getEmail(),
                    savedUser.getRole()
            )
    );
}

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User loginRequest) {

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            )
    );

    User user = userService.findByEmail(loginRequest.getEmail());

    String token = jwtUtil.generateToken(
            user.getId(),
            user.getEmail(),
            user.getRole()
    );

    return ResponseEntity.ok(
            new AuthResponse(
                    token,
                    user.getId(),
                    user.getEmail(),
                    user.getRole()
            )
    );
}
