package com.project.board.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestRequestRegisterDTO {
    private String username;
    private String email;
    private String password;
}
