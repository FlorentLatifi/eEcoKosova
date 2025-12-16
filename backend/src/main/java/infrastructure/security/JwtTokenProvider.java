package eco.kosova.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    
    @Value("${jwt.secret:ecokosova-secret-key-change-this-in-production-minimum-256-bits}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}") // 24 hours default
    private long jwtExpirationMs;
    
    private SecretKey getSigningKey() {
        // Ensure key is at least 256 bits (32 bytes)
        String key = jwtSecret;
        if (key.length() < 32) {
            key = key + "0".repeat(32 - key.length());
        }
        return Keys.hmacShaKeyFor(key.substring(0, 32).getBytes());
    }
    
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        
        return Jwts.builder()
            .subject(username)
            .claim("role", role)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey())
            .compact();
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        
        return claims.getSubject();
    }
    
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        
        return claims.get("role", String.class);
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
    
    public long getExpirationTime() {
        return jwtExpirationMs;
    }
}

