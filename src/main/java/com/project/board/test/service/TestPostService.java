package com.project.board.test.service;

import com.project.board.test.dto.post.TestCreatePostRequestDTO;
import com.project.board.test.dto.post.TestPostComResponseDTO;
import com.project.board.test.dto.post.TestPostResponseDTO;
import com.project.board.test.dto.post.TestUpdatePostRequestDTO;
import com.project.board.test.entity.TestMember;
import com.project.board.test.entity.TestPost;
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
public class TestPostService {
    private final TestPostRepository testPostRepository;
    private final TestMemberRepository testMemberRepository;
    public TestPostResponseDTO createPost(TestCreatePostRequestDTO requestDTO, String username) {
        TestMember author = testMemberRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        TestPost post = TestPost.builder()
                .title(requestDTO.getTitle())
                .author(author)
                .createAt(LocalDateTime.now())
                .content(requestDTO.getContent())
                .updateAt(LocalDateTime.now())
                .build();

        testPostRepository.save(post);

        return TestPostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(author.getUsername())
                .createdAt(post.getCreateAt())
                .updatedAt(post.getUpdateAt())
                .build();
    }


    public List<TestPostResponseDTO> getAllInfo() {
        return testPostRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"))
                .stream()
                .map(post -> TestPostResponseDTO
                        .builder()
                        .id(post.getId())
                        .author(post.getAuthor().getUsername())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .updatedAt(post.getUpdateAt())
                        .createdAt(post.getCreateAt())
                        .build()
                ).collect(Collectors.toList());
    }

    public TestPostResponseDTO getOneInfo(Long id) {
        TestPost post = testPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당게시물은 존재하지 않습니다."));

        return TestPostResponseDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor().getUsername())
                .createdAt(post.getCreateAt())
                .updatedAt(post.getUpdateAt())
                .id(post.getId())
                .build();
    }

    public TestPostResponseDTO updatePost(Long postId, String username, TestUpdatePostRequestDTO requestDTO) {
        TestPost post = testPostRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        if(!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("해당 게시물을 수정할 권한이 없습니다.");
        }

        TestPost posts = TestPost.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .updateAt(LocalDateTime.now())
                .build();
        return TestPostResponseDTO.builder()
                .id(posts.getId())
                .title(posts.getTitle())
                .content(posts.getContent())
                .createdAt(posts.getCreateAt())
                .updatedAt(posts.getUpdateAt())
                .build();


    }

    public TestPostComResponseDTO deletePost(Long postId, String username) {
        TestPost post = testPostRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        if(!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("해당 게시물을 수정할 권한이 없습니다.");
        }
        testPostRepository.delete(post);

        return TestPostComResponseDTO.builder()
                .message("삭제완료")
                .status(200)
                .build();
    }
}
