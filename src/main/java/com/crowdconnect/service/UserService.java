package com.crowdconnect.service;

import java.util.List;
import java.util.Optional;

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
        user.setSecurityQuestion(userDto.getSecurityQuestion());
        user.setSecurityAnswer(passwordEncoder.encode(userDto.getSecurityAnswer())); // Store the encoded answer
        user.setActive(true); // Set isActive to true on registration
        try {
            user.setRole(Role.valueOf(userDto.getRole().toUpperCase())); // Convert to enum
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided");
        }
        return userRepository.save(user);
    }

    public User authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setActive(true); // Set user as active upon successful login
            userRepository.save(user); // Save the active status update
            return user;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
    
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    

    public User findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null); // or throw an exception if you prefer
    }
    
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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
