package com.project.board.controller;

import com.project.board.service.MemberService;
import com.project.board.service.PostLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final MemberService memberService;

    @PostMapping("/likes")
    public ResponseEntity<Map<String, String>> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }
        Long memberId = memberService.getMemberIdByUsername(userDetails.getUsername());

        postLikeService.likePost(postId, memberId);
        return ResponseEntity.ok(Map.of("message", "좋아요 성공"));
    }

    @DeleteMapping("/likes")
    public ResponseEntity<Map<String, String>> unlikePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }
        Long memberId = memberService.getMemberIdByUsername(userDetails.getUsername());

        postLikeService.unlikePost(postId, memberId);
        return ResponseEntity.ok(Map.of("likes", "좋아요 취소 성공"));
    }

    @GetMapping("/likes")
    public ResponseEntity<Map<String, Integer>> countLikes(@PathVariable Long postId) {
        int count = postLikeService.countLikes(postId);
        return ResponseEntity.ok(Map.of("likes", count));
    }

    @GetMapping("/liked")
    public ResponseEntity<Map<String, Boolean>> isLiked(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다");
        }
        Long memberId = memberService.getMemberIdByUsername(userDetails.getUsername());
        boolean isLiked = postLikeService.isPostLiked(postId, memberId);
        return ResponseEntity.ok(Map.of("liked", isLiked));
    }
}

