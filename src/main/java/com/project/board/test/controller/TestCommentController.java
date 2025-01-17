package com.project.board.test.controller;

import com.project.board.test.dto.comment.TestCommentDeleteDTO;
import com.project.board.test.dto.comment.TestCommentResponseDTO;
import com.project.board.test.dto.comment.TestCommentUpdateRequestDTO;
import com.project.board.test.dto.comment.TestCreateCommentRequestDTO;
import com.project.board.test.repository.TestCommentRepository;
import com.project.board.test.service.TestCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tests/comments")
public class TestCommentController {
    private final TestCommentService testCommentService;

    @PostMapping("/{id}")
    public ResponseEntity<TestCommentResponseDTO> createComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TestCreateCommentRequestDTO requestDTO) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 유저가 아닙니다.");
        }
        TestCommentResponseDTO res = testCommentService.createComment(id,userDetails.getUsername(), requestDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TestCommentResponseDTO>> getComments(@PathVariable Long id) {

        List<TestCommentResponseDTO> res = testCommentService.getComments(id);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestCommentResponseDTO> updateComments(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TestCommentUpdateRequestDTO requestDTO) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 유저가 아닙니다.");
        }
        TestCommentResponseDTO res = testCommentService.updateComments(id, userDetails.getUsername(), requestDTO);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TestCommentDeleteDTO> deleteComments(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 유저가 아닙니다.");
        }
        TestCommentDeleteDTO res = testCommentService.deleteComments(id, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }
}
