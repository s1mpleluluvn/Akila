/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.util;

/**
 *
 * @author s1mpl
 */
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.Value;

public class JwtUtil {

    // Secret key for signing JWT (use a secure key in production)
    private static final String SECRET_KEY = "4b2aa3baedead6467ba5f87d66ef22876279c0eddae39de3263c02fcd254a86f539e80499516377b37d33263c40cec64808fa1f43183674676487d4be84c1c96d07017b23dd1f48d401039d8d44a78c9747e9e0b1c58e322f7dd9b08e2f742f261e8d81b81824478b8ef7dbe44ff5734e9f28c7c8263f403de8f369d78ef9db9949d4abffaa9dad5b1271033d8a7d7483079f88c57feae4df303c9da031152cd6ed6d239d8891b5e1add0edb21f77de2a38f2250149104c2768d0f55d633b0415ea8d73dd69003e306c33a5b8d1f3b2d88c0fe5b04bb99c45d0836b837c4892dcf1713fd3e6a61f002afc12dbe02e432aff0c990096be5d2d7e156e44af5d537";
    // Expiration time in milliseconds (e.g., 1 hour)
    private static final long EXPIRATION_TIME = 3600000; // 1 hour

    public static String generateToken(String username, UUID uuid, boolean rememberMe) {
        // Calculate expiration date
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);
        if (rememberMe) {
            expirationDate = new Date(now.getTime() + EXPIRATION_TIME * 24 * 30);
        }

        // Build JWT
        return Jwts.builder()
            .setSubject(username) // Set username as subject
            .claim("uuid", uuid.toString()) // Add UUID as a custom claim
            .setIssuedAt(now) // Set issued date
            .setExpiration(expirationDate) // Set expiration date
            .signWith(getSigningKey()) // Sign with HMAC-SHA256
            .compact(); // Convert to a compact string
    }
    
    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static Optional<String> authenticateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userName = claims.getSubject(); // Username is stored in `sub` (subject)
            return Optional.ofNullable(userName);
        } catch (Exception e) {
            return Optional.empty(); // Token không hợp lệ hoặc đã hết hạn
        }
    }
}