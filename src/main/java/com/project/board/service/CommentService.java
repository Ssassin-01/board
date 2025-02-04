package com.project.board.service;

import com.project.board.dto.comment.CommentDeleteResponseDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 📝 부모 댓글 생성
    public CommentResponseDTO createComment(Long postId, String username, CommentRequestDTO requestDTO) {
        Member author = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(requestDTO.getContent())
                .author(author)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor().getUsername())
                .postId(postId)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    // 📝 대댓글 생성
    public CommentResponseDTO createReply(Long postId, Long parentId, String username, CommentRequestDTO requestDTO) {
        Member author = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));

        // 대댓글 생성
        Comment reply = Comment.builder()
                .content(requestDTO.getContent())
                .author(author)
                .post(post)
                .parent(parentComment) // 부모 댓글 설정
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(reply);

        return CommentResponseDTO.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .author(reply.getAuthor().getUsername())
                .postId(postId)
                .parentId(parentId) // 부모 댓글 ID 포함
                .createdAt(reply.getCreatedAt())
                .build();
    }

    // 📝 특정 게시물의 부모 댓글 및 대댓글 조회
    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        return commentRepository.findByPostAndParentIsNullOrderByCreatedAtDesc(post)
                .stream()
                .map(comment -> CommentResponseDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .author(comment.getAuthor().getUsername())
                        .postId(post.getId())
                        .createdAt(comment.getCreatedAt())
                        .replies(comment.getReplies().stream() // 대댓글 리스트 포함
                                .map(reply -> CommentResponseDTO.builder()
                                        .id(reply.getId())
                                        .content(reply.getContent())
                                        .author(reply.getAuthor().getUsername())
                                        .postId(reply.getPost().getId())
                                        .parentId(reply.getParent().getId())
                                        .createdAt(reply.getCreatedAt())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    // 📝 댓글 수정
    public CommentResponseDTO updateComment(Long commentId, String username, CommentRequestDTO requestDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setContent(requestDTO.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor().getUsername())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    // 📝 댓글 삭제
    public CommentDeleteResponseDTO deleteComment(Long id, String username) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
        return new CommentDeleteResponseDTO("댓글이 성공적으로 삭제되었습니다.", 200);
    }
}