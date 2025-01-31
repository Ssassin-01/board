package com.project.board.security;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Setter
@Getter
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    /**
     * Access Token 생성
     */
    public String generateAccessToken(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("사용자 이름이 비어 있습니다.");
        }

        // 현재 시간
        Date now = new Date();
        // 만료 시간 계산
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        // 로그 추가
        log.debug("Access Token 발행: username={}, 발행 시간={}, 만료 시간={}",
                username, now, expiryDate);

        return Jwts.builder()
                .setSubject(username) // 사용자 정보 저장
                .setIssuedAt(now) // 발행 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secret) // 서명 키 사용
                .compact();
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        log.debug("Refresh Token 발행: username={}, 발행 시간={}, 만료 시간={}",
                username, now, expiryDate);

        return Jwts.builder()
                .setSubject(username) // 사용자 정보 저장
                .setIssuedAt(now) // 발행 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secret) // 서명 키 사용
                .compact();
    }

    /**
     * 토큰에서 사용자 이름 추출
     */
    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret) // 서명 키 사용
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException ex) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.", ex);
        }
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .setAllowedClockSkewSeconds(60) // 허용 시간 편차: 60초
                    .parseClaimsJws(token);
            log.debug("토큰 검증 성공");
            return true; // 유효한 토큰
        } catch (ExpiredJwtException ex) {
            log.warn("토큰이 만료되었습니다. 만료 시간: {}", ex.getClaims().getExpiration());
            return false; // 만료된 토큰은 false 반환
        } catch (MalformedJwtException ex) {
            log.error("잘못된 JWT 형식입니다.", ex);
            throw new IllegalArgumentException("잘못된 JWT 형식입니다.", ex);
        } catch (SignatureException ex) {
            log.error("JWT 서명이 유효하지 않습니다.", ex);
            throw new IllegalArgumentException("JWT 서명이 유효하지 않습니다.", ex);
        } catch (Exception ex) {
            log.error("토큰 검증 중 알 수 없는 오류 발생", ex);
            throw new IllegalStateException("토큰 검증 중 오류가 발생했습니다.", ex);
        }
    }
}
