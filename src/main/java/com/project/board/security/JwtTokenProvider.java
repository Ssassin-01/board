package com.project.board.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenProvider {

    private String secret;
    private long expiration;

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    // JWT 토큰 생성
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return io.jsonwebtoken.Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        return io.jsonwebtoken.Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(secret))
                    .parseClaimsJws(token);
            return true; // 유효하면 true 반환
        } catch (SignatureException ex) {
            throw new RuntimeException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new RuntimeException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Expired JWT token");
        }
    }
}
