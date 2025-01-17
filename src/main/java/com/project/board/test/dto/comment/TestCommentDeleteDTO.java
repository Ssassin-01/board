package com.project.board.test.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TestCommentDeleteDTO {
    private String message;
    private int status;
}
