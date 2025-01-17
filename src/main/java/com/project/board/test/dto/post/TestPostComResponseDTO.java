package com.project.board.test.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TestPostComResponseDTO {
    private String message;
    private int status;
}
