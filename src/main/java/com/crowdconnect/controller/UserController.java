package com.crowdconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crowdconnect.dto.JWTResponse;
import com.crowdconnect.dto.LoginRequest;
import com.crowdconnect.dto.UserDto;
import com.crowdconnect.model.User;
import com.crowdconnect.service.JwtUtils;
import com.crowdconnect.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        if (userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }
        userService.registerUser(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        String jwt = jwtUtils.generateJwtToken(user);
        return ResponseEntity.ok(new JWTResponse(jwt));
    }
}
