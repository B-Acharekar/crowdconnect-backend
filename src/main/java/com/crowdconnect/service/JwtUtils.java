package com.crowdconnect.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.crowdconnect.model.User;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {

    // Generate a secure key
    private final byte[] jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();

    public String generateJwtToken(User user) {
        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + 86400000)) // Token valid for 1 day
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
    }
}
