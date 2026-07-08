package com.stocksim.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretString = "your-very-long-and-secure-secret-key-that-is-at-least-32-bytes";
    private final SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));

    // 🌟 시간 설정 분리 (밀리초 단위)
    private final long accessTokenValidity = 1800000;      // 30분
    private final long refreshTokenValidity = 1209600000;  // 14일 (2주일)

    // 🌟 1. 짧은 토큰 (Access Token) 생성
    public String createAccessToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    // 🌟 2. 긴 토큰 (Refresh Token) 생성
    // 누구의 토큰인지 식별하기 위해 똑같이 email을 과자로 구워놓습니다.
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    // 토큰에서 유저 이메일(Subject)을 꺼내는 메서드 (기존 유지)
    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 토큰이 유효한지 검증하는 메서드 (기존 유지)
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}