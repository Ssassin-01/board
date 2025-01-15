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

    @PostMapping("/{postId}/create")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long postId,
                                                            @AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody CommentRequestDTO requestDTO) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }
        CommentResponseDTO res = commentService.createComment(postId, userDetails.getUsername(), requestDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        CommentResponseDTO res = commentService.updateComment(id, userDetails.getUsername(),commentRequestDTO);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentDeleteResponseDTO> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        CommentDeleteResponseDTO res = commentService.deleteComment(id, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }
}
