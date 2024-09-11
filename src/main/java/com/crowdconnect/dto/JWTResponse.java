package com.crowdconnect.dto;

public class JWTResponse {
    private String token;

    public JWTResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}