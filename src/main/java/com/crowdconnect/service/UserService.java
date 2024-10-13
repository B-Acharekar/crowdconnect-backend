package com.crowdconnect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crowdconnect.dto.UserDto;
import com.crowdconnect.exception.ResourceNotFoundException;
import com.crowdconnect.model.Role;
import com.crowdconnect.model.User;
import com.crowdconnect.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActive(true); // Set isActive to true on registration
        try {
            user.setRole(Role.valueOf(userDto.getRole().toUpperCase())); // Convert to enum
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided");
        }        return userRepository.save(user);
    }

    public User authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Fetch all users from the database
    }
    
    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue(); // Fetch only active users
    }

    
    public void assignRole(Long userId, Role role) {
    	User user = userRepository.findById(userId)
 	    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }
}