package com.project.board.test.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestLoginRequestDTO {
    private String username;
    private String email;
    private String password;
}
