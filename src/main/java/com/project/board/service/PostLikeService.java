package com.project.board.service;

import com.project.board.entity.Member;
import com.project.board.entity.Post;
import com.project.board.entity.PostLike;
import com.project.board.repository.MemberRepository;
import com.project.board.repository.PostLikeRepository;
import com.project.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void likePost(Long postId, Long memberId) {
        try {
            if (postId == null) {
                throw new IllegalArgumentException("postId가 null입니다.");
            }
            if (memberId == null) {
                throw new IllegalArgumentException("memberId가 null입니다.");
            }

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

            if (postLikeRepository.existsByPostIdAndMemberId(postId, memberId)) {
                throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
            }

            PostLike postLike = PostLike.builder()
                    .post(post)
                    .member(member)
                    .build();
            postLikeRepository.save(postLike);

            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);

            log.info("✅ 좋아요 추가 성공! postId={}, memberId={}", postId, memberId);
        } catch (Exception e) {
            log.error("❌ 좋아요 추가 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("좋아요 추가 중 서버 오류 발생", e);
        }
    }

    @Transactional
    public void unlikePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        if(!postLikeRepository.existsByPostIdAndMemberId(postId, memberId)) {
            throw new IllegalArgumentException("좋아요를 누르지 않았습니다.");
        }
        postLikeRepository.deleteByPostIdAndMemberId(postId,memberId);
        post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        postRepository.save(post);
    }

    public int countLikes(Long postId) {
        return postRepository.findById(postId)
                .map(Post::getLikeCount)
                .orElse(0);
    }

    public boolean isPostLiked(Long postId, Long memberId) {
        return postLikeRepository.existsByPostIdAndMemberId(postId, memberId);
    }
}
