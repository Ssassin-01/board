package com.project.board.test.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TestCommentResponseDTO {
    private Long id;
    private String content;
    private String author;
    private Long post;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
