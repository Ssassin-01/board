package com.project.board.test.controller;

import com.project.board.test.dto.post.TestCreatePostRequestDTO;
import com.project.board.test.dto.post.TestPostComResponseDTO;
import com.project.board.test.dto.post.TestPostResponseDTO;
import com.project.board.test.dto.post.TestUpdatePostRequestDTO;
import com.project.board.test.repository.TestPostRepository;
import com.project.board.test.service.TestPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tests/posts")
public class TestPostController {
    private final TestPostService testPostService;

    @PostMapping("/create")
    public ResponseEntity<TestPostResponseDTO> createPost(
            @RequestBody TestCreatePostRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }
        TestPostResponseDTO res = testPostService.createPost(requestDTO, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<TestPostResponseDTO>> postAllInfo() {
        List<TestPostResponseDTO> res = testPostService.getAllInfo();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<TestPostResponseDTO> postOneInfo(@PathVariable Long postId) {
        TestPostResponseDTO res = testPostService.getOneInfo(postId);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<TestPostResponseDTO> updatePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TestUpdatePostRequestDTO requestDTO
    ) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 유저가 아닙니다.");
        }
        TestPostResponseDTO res = testPostService.updatePost(postId, userDetails.getUsername(), requestDTO);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<TestPostComResponseDTO> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 유저가 아닙니다.");
        }
        TestPostComResponseDTO res = testPostService.deletePost(postId, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }
}
