package com.project.board.dto;

import jakarta.validation.constraints.Email;
import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDTO {
    @NotNull(message = "username 필수입니다.")
    private String username;

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotNull(message = "password 필수입니다.")
    private String password;
}

// 회원가입: username, email, password 사용
// 로그인: username, password 사용
// 회원 수정: 상황에 따라 필드만 사용