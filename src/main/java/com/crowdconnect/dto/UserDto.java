package com.crowdconnect.dto;

public class UserDto {
    private String username;
    private String email;  // Added email field
    private String password;
    private String role; // Add role field

    // Getters and Setters
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
}