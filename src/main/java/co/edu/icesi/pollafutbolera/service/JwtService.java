package co.edu.icesi.pollafutbolera.service;

import org.springframework.stereotype.Service;

import co.edu.icesi.pollafutbolera.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String extractUsername(final String token) {

        final Claims jwtToken = Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return jwtToken.getSubject();
    }

    public Long extractUserId(final String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(jwtToken.getId());
    }

    public boolean isTokenValid(final String token, final User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getNickname()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(final String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return jwtToken.getExpiration().before(new Date());
    }

    public String generateToken(final User user) {
        return buildToken(user, jwtExpiration);
    }

    private String buildToken(final User user, final Long expiration) {
        return Jwts.builder()
                .id(user.getId().toString())
                .claims(Map.of("role", user.getRole().getName()))
                .subject(user.getNickname())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey())
                .compact();
    }

    private SecretKey secretKey() {
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }
}
