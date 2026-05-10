package com.ds.mall.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenService {

    @Value("${mall.jwt.secret}")
    private String secret;

    @Value("${mall.jwt.expire-seconds}")
    private Long expireSeconds;

    public String createToken(LoginUser loginUser) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + expireSeconds * 1000);
        return Jwts.builder()
                .subject(String.valueOf(loginUser.getUserId()))
                .claim("username", loginUser.getUsername())
                .claim("roles", loginUser.getRoles())
                .issuedAt(now)
                .expiration(expireAt)
                .signWith(key())
                .compact();
    }

    @SuppressWarnings("unchecked")
    public LoginUser parseToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        Long userId = Long.valueOf(claims.getSubject());
        String username = claims.get("username", String.class);
        List<String> roles = (List<String>) claims.get("roles", List.class);
        return new LoginUser(userId, username, null, 1, roles);
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
