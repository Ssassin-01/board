package com.project.board.test.service;

import com.project.board.security.JwtTokenProvider;
import com.project.board.test.dto.*;
import com.project.board.test.entity.TestMember;
import com.project.board.test.repository.MemberTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberTestService {
    private final MemberTestRepository memberTestRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignupTestResponseDTO signUp(SignUpTestDTO signUpTestDTO) {
        //1. 이미 username이 존재하는가?
        if(memberTestRepository.findByUsername(signUpTestDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("해당 유저네임은 존재하는 네임입니다.");
        }
        //2. 이미 email이 존재하는가?
        if(memberTestRepository.findByEmail(signUpTestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("해당 이메일은 존재합니다.");
        }
        //3. 비번은 보안해야지
        String encodePassword = passwordEncoder.encode(signUpTestDTO.getPassword());

        //4. Member에 값 넣기
        TestMember member = TestMember.builder()
                .username(signUpTestDTO.getUsername())
                .password(encodePassword)
                .email(signUpTestDTO.getEmail())
                .build();

        //5. 저장
        memberTestRepository.save(member);

        //6. Member를 DTO로 반환(message, status)
        return new SignupTestResponseDTO("회원가입 성공", 201);
    }

    //로그인
    public LoginResponseTestDTO login(LoginRequestTestDTO loginRequestDTO) {
        //1. 있는 username인가?
        TestMember member = memberTestRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        //2. 패스워드가 일치하는가?
        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
        //3. 토큰 발행 시켜줌
        String token = jwtTokenProvider.generateToken(member.getUsername());

        //4. LoginResponseDTO에 토큰 담아서 반환시키기(username, token, role)
        return new LoginResponseTestDTO(member.getUsername(), token, "USER");
    }

    //유저 조회하기
    //1. 현재 존재하는 username인가?
    //2. 맞으면 MemberDTO로 반환(id, username, password)
    public MemberTestDTO memberTestInfo(String username) {
        TestMember member =  memberTestRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없다" + username));

        return new MemberTestDTO(member.getId(), member.getUsername(), member.getPassword());
    }
}
