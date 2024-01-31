package com.authapi.authapi;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class TokenUtils {

    public static String createToken(String secret, Map<String, Object> claims, String subject, Date issuedAt, Date expiration) {
        // Convertir la clave secreta a un objeto Key
        byte[] secretBytes = Base64.getDecoder().decode(secret);
        Key key = Keys.hmacShaKeyFor(secretBytes);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token, String secret) {
        // Convertir la clave secreta a un objeto Key
        byte[] secretBytes = Base64.getDecoder().decode(secret);
        Key key = Keys.hmacShaKeyFor(secretBytes);

        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
