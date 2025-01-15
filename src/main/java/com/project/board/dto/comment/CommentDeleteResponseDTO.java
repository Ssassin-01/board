package com.project.board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDeleteResponseDTO {
    private String message;
    private int status;
}
