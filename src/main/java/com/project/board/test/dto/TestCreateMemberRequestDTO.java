package com.project.board.test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestCreateMemberRequestDTO {
    private String username;
    private String email;
    private String password;
}
