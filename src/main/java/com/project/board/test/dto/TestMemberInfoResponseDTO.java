package com.project.board.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TestMemberInfoResponseDTO {
    private Long id;
    private String message;
    private int status;
    private String username;
    private String email;
}
