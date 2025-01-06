package com.project.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message; // 오류 메시지
    private int status; // HTTP 상태 코드
}