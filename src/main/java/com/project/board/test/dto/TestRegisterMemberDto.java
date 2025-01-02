package com.project.board.test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestRegisterMemberDto {
    private String username;
    private String email;
    private String password;
}