package com.grupo9.auto_repair_shop.security;

import com.grupo9.auto_repair_shop.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final String REFRESH_TOKEN_TYPE = "REFRESH";

    private final SecretKey key;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long accessExpiration,
            @Value("${jwt.refresh-expiration-ms}") long refreshExpiration
    ) {
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    public String generateAccessToken(User user) {
        return generateToken(user, ACCESS_TOKEN_TYPE, accessExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, REFRESH_TOKEN_TYPE, refreshExpiration);
    }

    private String generateToken(User user, String tokenType, long expiration) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        Long tokenVersion = user.getTokenVersion() == null
                ? 0L
                : user.getTokenVersion();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("type", tokenType)
                .claim("tokenVersion", tokenVersion)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractTokenType(String token) {
        return extractAllClaims(token).get("type", String.class);
    }

    public Long extractTokenVersion(String token) {
        Object value = extractAllClaims(token).get("tokenVersion");

        if (value instanceof Number number) {
            return number.longValue();
        }

        if (value instanceof String text) {
            return Long.parseLong(text);
        }

        return null;
    }

    public boolean isAccessToken(String token) {
        try {
            return ACCESS_TOKEN_TYPE.equals(extractTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            return REFRESH_TOKEN_TYPE.equals(extractTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getAccessExpiration() {
        return accessExpiration;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}