package com.project.board.test.service;

import com.project.board.security.JwtTokenProvider;
import com.project.board.test.dto.*;
import com.project.board.test.entity.TestMember;
import com.project.board.test.repository.TestMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestMemberService {
    private final TestMemberRepository testMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    public TestCommonResponseDTO createMember(TestCreateMemberRequestDTO requestDTO) {
        if(testMemberRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("해당 이메일이 존재합니다.");
        }
        //유저네임이 존재하니?
        if(testMemberRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("해당 유저이름이 존재하비다.");
        }
        //없으면 저장하자
        TestMember member = TestMember.builder()
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .build();

        testMemberRepository.save(member);

        //다 되면 완료 되었다고 리턴을 해주자
        return TestCommonResponseDTO.builder()
                .message("회원가입 완료")
                .status(200)
                .build();
    }

    public TestMemberResponseDTO loginMember(TestMemberSignUpRequestDTO requestDTO) {
        // 로그인할려는 유저가 있냐?
        TestMember member = testMemberRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(()-> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        if(!passwordEncoder.matches(requestDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
        //없으면 토큰 발급해주자
        String token =  jwtTokenProvider.generateToken(member.getUsername());

        return TestMemberResponseDTO.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .token(token)
                .build();
    }

    public TestMemberInfoResponseDTO info(String username) {
        TestMember member = testMemberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        return TestMemberInfoResponseDTO.builder()
                .id(member.getId())
                .message("조회완료")
                .status(200)
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }

    public TestMemberInfoResponseDTO updateMember(String username, TestCreateMemberRequestDTO requestDTO) {
        TestMember member = testMemberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        member.setEmail(requestDTO.getEmail());
        member.setUsername(requestDTO.getUsername());
        member.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        testMemberRepository.save(member);

        return TestMemberInfoResponseDTO.builder()
                .id(member.getId())
                .message("수정완료")
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }

    public TestCommonResponseDTO deleteMember(String username) {
        TestMember member = testMemberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        testMemberRepository.delete(member);

        return TestCommonResponseDTO.builder()
                .message("삭제 완료")
                .status(200)
                .build();
    }
}
