package com.project.board.controller;

import com.project.board.dto.comment.CommentDeleteResponseDTO;
import com.project.board.dto.comment.CommentRequestDTO;
import com.project.board.dto.comment.CommentResponseDTO;
import com.project.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 📝 부모 댓글 생성
    @PostMapping("/{postId}/create")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long postId,
                                                            @AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody CommentRequestDTO requestDTO) {
        if (userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }
        CommentResponseDTO res = commentService.createComment(postId, userDetails.getUsername(), requestDTO);
        return ResponseEntity.ok(res);
    }

    // 📝 대댓글 생성
    @PostMapping("/{postId}/{parentId}/create-reply")
    public ResponseEntity<CommentResponseDTO> createReply(@PathVariable Long postId,
                                                          @PathVariable Long parentId,
                                                          @AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody CommentRequestDTO requestDTO) {
        if (userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }
        CommentResponseDTO res = commentService.createReply(postId, parentId, userDetails.getUsername(), requestDTO);
        return ResponseEntity.ok(res);
    }

    // 📝 특정 게시물의 댓글 및 대댓글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 📝 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        CommentResponseDTO res = commentService.updateComment(id, userDetails.getUsername(), commentRequestDTO);
        return ResponseEntity.ok(res);
    }

    // 📝 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentDeleteResponseDTO> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        CommentDeleteResponseDTO res = commentService.deleteComment(id, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }
}
