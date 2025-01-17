package com.project.board.test.service;

import com.project.board.test.dto.comment.TestCommentDeleteDTO;
import com.project.board.test.dto.comment.TestCommentResponseDTO;
import com.project.board.test.dto.comment.TestCommentUpdateRequestDTO;
import com.project.board.test.dto.comment.TestCreateCommentRequestDTO;
import com.project.board.test.entity.TestComment;
import com.project.board.test.entity.TestMember;
import com.project.board.test.entity.TestPost;
import com.project.board.test.repository.TestCommentRepository;
import com.project.board.test.repository.TestMemberRepository;
import com.project.board.test.repository.TestPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestCommentService {
    private final TestCommentRepository testCommentRepository;
    private final TestMemberRepository testMemberRepository;
    private final TestPostRepository testPostRepository;
    public TestCommentResponseDTO createComment(Long id, String username, TestCreateCommentRequestDTO requestDTO) {
        TestMember author = testMemberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당유저는 존재하지 않습니다."));

        TestPost post = testPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다."));

        TestComment comment = TestComment.builder()
                .content(requestDTO.getComment())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .author(author)
                .post(post)
                .build();

        testCommentRepository.save(comment);

        return TestCommentResponseDTO.builder()
                .id(comment.getId())
                .createdAt(comment.getCreateAt())
                .updateAt(comment.getUpdateAt())
                .post(comment.getPost().getId())
                .author(comment.getAuthor().getUsername())
                .build();
    }


    public List<TestCommentResponseDTO> getComments(Long id) {
        TestPost post = testPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지않습니다."));

        return testCommentRepository.findByPost(post)
                .stream()
                .map((comment) -> TestCommentResponseDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreateAt())
                        .updateAt(comment.getUpdateAt())
                        .author(comment.getAuthor().getUsername())
                        .build())
                .collect(Collectors.toList());

    }

    public TestCommentResponseDTO updateComments(Long id, String username, TestCommentUpdateRequestDTO requestDTO) {
        TestComment comment = testCommentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if(!comment.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("해당 댓글의 수정권한이 없습니다.");
        }

        comment.setContent(requestDTO.getContent());
        comment.setUpdateAt(LocalDateTime.now());
        testCommentRepository.save(comment);

        return TestCommentResponseDTO.builder()
                .id(comment.getId())
                .post(comment.getPost().getId())
                .author(comment.getAuthor().getUsername())
                .updateAt(comment.getUpdateAt())
                .createdAt(comment.getCreateAt())
                .content(comment.getContent())
                .build();
    }

    public TestCommentDeleteDTO deleteComments(Long id, String username) {
        TestComment comment = testCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));
        if(!comment.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("해당 댓글 삭제 권한이 없습니다.");
        }
        testCommentRepository.delete(comment);
        return TestCommentDeleteDTO.builder()
                .message("삭제완료")
                .status(200)
                .build();
    }
}
