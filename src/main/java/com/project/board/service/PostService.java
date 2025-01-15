package com.project.board.service;

import com.project.board.dto.post.PostDeleteResponseDTO;
import com.project.board.dto.post.PostRequestDTO;
import com.project.board.dto.post.PostResponseDTO;
import com.project.board.dto.post.PostUpdateRequestDTO;
import com.project.board.entity.Member;
import com.project.board.entity.Post;
import com.project.board.repository.MemberRepository;
import com.project.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    public PostResponseDTO createPost(String username, PostRequestDTO requestDTO) {
        //작성자 정보 가져오기
        Member author = memberRepository.findByUsername(username)
                .orElseThrow(()->new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        //게시물 저장
        Post post = Post.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .author(author)
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);

        //PostResponseDTO로 리턴시키기
        return PostResponseDTO.
                builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor().getUsername())
                .createdAt(post.getCreatedAt())
                .updatedAt(null)
                .build();
    }


    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(post -> PostResponseDTO
                        .builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getAuthor().getUsername())
                        .createdAt(post.getCreatedAt())
                        .build())
                .toList();
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor().getUsername())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public PostResponseDTO updatePost(Long id, PostUpdateRequestDTO requestDTO, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        if(!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("게시물을 수정할 권한이 없습니다.");
        }

        post.setTitle(requestDTO.getTitle());
        post.setContent(requestDTO.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);

        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .author(post.getAuthor().getUsername())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public PostDeleteResponseDTO deletePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if(!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("게시물을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
        return new PostDeleteResponseDTO("게시물이 성공적으로 삭제되었습니다.", 100);
    }
}
