package com.project.board.controller;

import com.project.board.dto.post.PostRequestDTO;
import com.project.board.dto.post.PostResponseDTO;
import com.project.board.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostResponseDTO> createPost(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody PostRequestDTO requestDTO) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        PostResponseDTO res = postService.createPost(userDetails.getUsername(), requestDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO res = postService.getPostById(id);
        return ResponseEntity.ok(res);
    }
}
