package com.project.board.test.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestResponseRegisterDTO {
    private String message;
    private int status;
}
