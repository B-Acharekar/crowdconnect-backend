package com.crowdconnect.service;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {

	private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	public String generateJwtToken(String username) {
	    return Jwts.builder()
	        .setSubject(username)
	        .setIssuedAt(new Date())
	        .setExpiration(new Date(new Date().getTime() + 86400000)) // 1 day
	        .signWith(secretKey)
	        .compact();
	}


	public boolean validateJwtToken(String token) {
	    try {
	        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
	        return true;
	    } catch (JwtException | IllegalArgumentException e) {
	        return false;
	    }
	}

    // Extract the username from the JWT token
    public String getUsernameFromJwtToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract a specific claim from the JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
