package com.project.board.service;

import com.project.board.dto.comment.CommentRequestDTO;
import com.project.board.dto.comment.CommentResponseDTO;
import com.project.board.entity.Comment;
import com.project.board.entity.Member;
import com.project.board.entity.Post;
import com.project.board.repository.CommentRepository;
import com.project.board.repository.MemberRepository;
import com.project.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentResponseDTO createComment(Long postId, String username, CommentRequestDTO requestDTO) {
        //작성자 조회
        Member author = memberRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));

        //게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        //댓글 생성 및 저장
        Comment comment = Comment.builder()
                .content(requestDTO.getContent())
                .author(author)
                .post(post)
                .createAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor().getUsername())
                .postId(postId)
                .createdAt(comment.getCreateAt())
                .build();
    }

    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        return commentRepository.findByPost(post)
                .stream()
                .map(comment -> CommentResponseDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .author(comment.getAuthor().getUsername())
                        .createdAt(comment.getCreateAt())
                        .postId(comment.getPost().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
