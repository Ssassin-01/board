package com.project.board.dto.comment;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    private Long id;
    private String content;
    private String author;
    private Long postId;
    private Long parentId; // 부모 댓글 ID (null이면 부모 댓글)
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간
    private List<CommentResponseDTO> replies; // 대댓글 리스트
}
