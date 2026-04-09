package com.github.maxiamikel.attendancemanagementapi.config;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.maxiamikel.attendancemanagementapi.dto.response.AccessToken;
import com.github.maxiamikel.attendancemanagementapi.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    @Value("${api.jwt-secret}")
    private String apiSecret;

    @Value("${api.jwt-expiration}")
    private Long apiExpiration;

    @Value("${api.jwt-issuer}")
    private String apiIssuer;

    public AccessToken generateToken(User user) {

        String token = Jwts
                .builder()
                .subject(user.getEmail())
                .signWith(getKey())
                .issuedAt(new Date())
                .expiration(getExpiration())
                .issuer(apiIssuer)
                .claims(getclaims(user))
                .compact();

        return new AccessToken(token);
    }

    private Map<String, String> getclaims(User user) {

        Map<String, String> claims = new HashMap<>();
        claims.put("name", user.getName());
        return claims;
    }

    private Date getExpiration() {
        return new Date(System.currentTimeMillis() + apiExpiration);
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(apiSecret.getBytes(StandardCharsets.UTF_8));
    }
}
