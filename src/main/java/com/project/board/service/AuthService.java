package com.project.board.service;

import com.project.board.dto.MemberRequestDTO;
import com.project.board.entity.Member;
import com.project.board.repository.MemberRepository;
import com.project.board.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.access-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    public void login(MemberRequestDTO requestDTO, HttpServletResponse response) {
        try {
            // 사용자 조회
            Member member = memberRepository.findByUsername(requestDTO.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 비밀번호 검증
            if (!passwordEncoder.matches(requestDTO.getPassword(), member.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            // 로그 추가
            log.info("사용자 [{}] 로그인 성공", member.getUsername());

            // Access Token 생성 (JwtTokenProvider 활용)
            String accessToken = jwtTokenProvider.generateAccessToken(member.getUsername());

            // Refresh Token 생성 (JwtTokenProvider 활용)
            String refreshToken = jwtTokenProvider.generateRefreshToken(member.getUsername());

            // Refresh Token을 Redis에 저장
            redisService.saveRefreshToken(member.getUsername(), refreshToken, refreshTokenExpiration);

            // 쿠키 설정
            setCookie(response, "accessToken", accessToken, accessTokenExpiration / 1000);

        } catch (Exception ex) {
            log.error("로그인 중 오류 발생: ", ex);
            throw ex;
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // JwtTokenProvider에서 토큰 추출
            String accessToken = jwtTokenProvider.getTokenFromRequest(request);
            if (accessToken == null) {
                throw new IllegalStateException("로그아웃 실패: Access Token이 요청에 포함되지 않았습니다.");
            }

            // Access Token에서 사용자 이름 추출
            String username = jwtTokenProvider.getUsernameFromToken(accessToken);
            log.info("로그아웃 요청: {}", username);

            // Redis에서 Refresh Token 삭제
            redisService.deleteRefreshToken(username);
            log.info("Redis에서 Refresh Token 삭제 완료: {}", username);

            // Access Token 쿠키 삭제
            deleteCookie(response, "accessToken");

            log.info("로그아웃 성공: {}", username);
        } catch (Exception ex) {
            log.error("로그아웃 처리 중 예외 발생: ", ex);
            throw new IllegalStateException("로그아웃 실패", ex);
        }
    }

    public String refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // 1️⃣ 쿠키에서 accessToken 가져오기
        String accessToken = getCookieValue(request, "accessToken");
        if (accessToken == null) {
            throw new IllegalArgumentException("Access Token이 존재하지 않습니다.");
        }

        // 2️⃣ accessToken에서 username 추출
        String username = jwtTokenProvider.getUsernameFromToken(accessToken);

        // 3️⃣ Redis에서 저장된 refreshToken 가져오기
        String storedRefreshToken = redisService.getRefreshToken(username);
        if (storedRefreshToken == null) {
            throw new IllegalArgumentException("Refresh Token이 존재하지 않습니다.");
        }

        // 4️⃣ 새로운 Access Token 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        // 5️⃣ 새로운 Access Token을 쿠키에 저장
        setCookie(response, "accessToken", newAccessToken, accessTokenExpiration / 1000);

        return newAccessToken;
    }

    public Map<String, Boolean> checkAuthStatus(HttpServletRequest request) {
        Map<String, Boolean> response = new HashMap<>();
        String accessToken = getCookieValue(request, "accessToken");

        if (accessToken != null) {
            boolean isValid = jwtTokenProvider.validateToken(accessToken);
            log.info("Access Token 상태: {}", isValid ? "유효함" : "유효하지 않음");
            response.put("isLoggedIn", isValid);
        } else {
            log.info("Access Token이 존재하지 않음 (쿠키 없음)");
            response.put("isLoggedIn", false);
        }

        return response;
    }



    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    private void setCookie(HttpServletResponse response, String name, String value, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) // 배포 시 true로 변경
                .path("/")
                .maxAge(maxAge) // 초 단위로 설정
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, null)
                .httpOnly(true)
                .secure(false) // 배포 시 true로 변경
                .path("/")
                .maxAge(0) // 즉시 만료
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}