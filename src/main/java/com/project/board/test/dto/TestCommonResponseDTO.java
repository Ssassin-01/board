package com.project.board.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TestCommonResponseDTO {
    private String message;
    private int status;
}
