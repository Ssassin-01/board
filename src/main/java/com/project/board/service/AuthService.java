package com.project.board.service;

import com.project.board.dto.MemberRequestDTO;
import com.project.board.entity.Member;
import com.project.board.repository.MemberRepository;
import com.project.board.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

            // Access Token 생성
            String accessToken = jwtTokenProvider.generateAccessToken(member.getUsername());

            // Refresh Token 생성
            String refreshToken = jwtTokenProvider.generateRefreshToken(member.getUsername());

            // Refresh Token을 Redis에 저장
            redisService.saveRefreshToken(member.getUsername(), refreshToken, refreshTokenExpiration);

            // Access Token 설정 (만료 시간 확인 및 기본 값 설정)
            long accessTokenMaxAge = jwtTokenProvider.getAccessTokenExpiration() / 1000;
            if (accessTokenMaxAge <= 0) {
                accessTokenMaxAge = 3600; // 기본 1시간 설정
            }

            // 쿠키 설정
            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(accessTokenMaxAge)
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", accessTokenCookie.toString());

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenExpiration / 1000) // 초 단위로 설정
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        } catch (Exception ex) {
            log.error("로그인 중 오류 발생: ", ex);
            throw ex;
        }
    }

    public void logout(String username, HttpServletResponse response) {
        try {
            log.info("로그아웃 요청: {}", username);

            // Redis에서 Refresh Token 삭제
            redisService.deleteRefreshToken(username);
            log.info("Redis에서 Refresh Token 삭제 완료: {}", username);

            // Access Token 및 Refresh Token 쿠키 삭제
            ResponseCookie deleteAccessTokenCookie = ResponseCookie.from("accessToken", null)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", deleteAccessTokenCookie.toString());

            ResponseCookie deleteRefreshTokenCookie = ResponseCookie.from("refreshToken", null)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", deleteRefreshTokenCookie.toString());

            log.info("로그아웃 성공: {}", username);
        } catch (Exception ex) {
            log.error("로그아웃 처리 중 예외 발생: ", ex);
            throw new IllegalStateException("로그아웃 실패", ex);
        }
    }


    public String refreshAccessToken(String refreshToken, HttpServletResponse response) {
        // Refresh Token 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        // Refresh Token에서 username 추출
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

        // Redis에서 저장된 Refresh Token과 비교
        String storedRefreshToken = redisService.getRefreshToken(username);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Redis에 저장된 Refresh Token과 일치하지 않습니다.");
        }

        // 새로운 Access Token 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        // Access Token 설정 (만료 시간 확인 및 기본 값 설정)
        long accessTokenMaxAge = jwtTokenProvider.getAccessTokenExpiration() / 1000;
        if (accessTokenMaxAge <= 0) {
            accessTokenMaxAge = 3600; // 기본 1시간 설정
        }

        // 새로운 Access Token을 HttpOnly 쿠키에 설정
        ResponseCookie newAccessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(accessTokenMaxAge)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", newAccessTokenCookie.toString());

        return newAccessToken; // 새로 생성된 Access Token 반환
    }

}
