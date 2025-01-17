package com.project.board.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TestMemberResponseDTO {
    private String email;
    private String username;
    private String token;
}
