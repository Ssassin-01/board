package com.project.board.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseTestDTO {
    private String username;
    private String token;
    private String role;
}
