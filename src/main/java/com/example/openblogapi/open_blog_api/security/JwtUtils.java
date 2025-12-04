package com.example.openblogapi.open_blog_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils
{

    private final Key key;
    private final long expirationMs;

    public JwtUtils(@Value("${app.jwt.secret}") String secret,
                    @Value("${app.jwt.expiration}") long expirationMs)
    {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    public String generateToken(String subject, Map<String, Object> claims)
    {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String token)
    {
        return parse(token).getBody().getSubject();
    }

    public boolean isValid(String token)
    {
        try
        {
            parse(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException ex)
        {
            return false;
        }
    }

    private Jws<Claims> parse(String token)
    {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
