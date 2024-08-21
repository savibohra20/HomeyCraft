package com.homeycraft.Cart_Service.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtService {

    private final String SECRET_KEY = "FYNDNA-SUCCESS"; // Ensure this key is securely managed and consistent

    public String extractEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getToken(String emailId, String userRole) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(emailId)
                .claim("role", userRole)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY) // Use the constant SECRET_KEY
                .compact();
    }

    public boolean validateToken(String token, String emailId, String role) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SECRET_KEY) // Use the constant SECRET_KEY
                    .parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            String tokenEmailId = claims.getSubject();
            String tokenRole = claims.get("role", String.class);

            // Debugging log
            System.out.println("Token email: " + tokenEmailId + ", Token role: " + tokenRole + ", Provided email: " + emailId + ", Provided role: " + role);

            return emailId.equalsIgnoreCase(tokenEmailId) && role.equalsIgnoreCase(tokenRole);
        } catch (JwtException | IllegalArgumentException e) {
            // Log the error for debugging purposes
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}

