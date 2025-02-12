package com.project.board.controller;

import com.project.board.dto.CommonResponseDTO;
import com.project.board.dto.MemberRequestDTO;
import com.project.board.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDTO<String>> login(@RequestBody MemberRequestDTO requestDTO, HttpServletResponse response) {
        authService.login(requestDTO, response);
        return ResponseEntity.ok(CommonResponseDTO.<String>builder()
                .message("로그인 성공!")
                .status(200)
                .data("AccessToken 및 RefreshToken이 설정되었습니다.")
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponseDTO<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(CommonResponseDTO.<String>builder()
                .message("로그아웃 성공!")
                .status(200)
                .data("토큰이 삭제되었습니다.")
                .build());
    }
    @GetMapping("/refresh")
    public ResponseEntity<CommonResponseDTO<String>> refreshAccessToken(
            HttpServletRequest request, HttpServletResponse response) {
        try {
            String newAccessToken = authService.refreshAccessToken(request, response);
            return ResponseEntity.ok(CommonResponseDTO.<String>builder()
                    .message("Access Token이 성공적으로 재발급되었습니다.")
                    .status(200)
                    .data(newAccessToken)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(CommonResponseDTO.<String>builder()
                    .message(e.getMessage())
                    .status(401)
                    .data(null)
                    .build());
        }
    }
}
