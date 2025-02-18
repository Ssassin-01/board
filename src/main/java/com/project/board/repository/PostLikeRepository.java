package com.project.board.repository;

import com.project.board.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndMemberId(Long postId, Long memberId);
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);
    int countByPostId(Long postId);
    void deleteByPostIdAndMemberId(Long postId, Long memberId);
}
