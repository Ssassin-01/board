package com.project.board.test.service;

import com.project.board.security.JwtTokenProvider;
import com.project.board.test.dto.TestLoginMemberDto;
import com.project.board.test.dto.TestRegisterMemberDto;
import com.project.board.test.entity.TestMember;
import com.project.board.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class TestMemberService {
    private final TestRepository testRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    //회원가입
    //유저 이름이 존재하냐?
    //이메일이 존재하냐?
    //패스워드는 Bcrpt 암호화
    //TestMember에 저장
    public void registerMember(TestRegisterMemberDto testRegisterMemberDto) {
        if(testRepository.findByUsername(testRegisterMemberDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("등록된 닉네임입니다.");
        }
        if(testRepository.findByEmail(testRegisterMemberDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("등록된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(testRegisterMemberDto.getPassword());

        TestMember testMember = TestMember.builder()
                .username(testRegisterMemberDto.getUsername())
                .email(testRegisterMemberDto.getEmail())
                .password(encodedPassword)
                .build();

        testRepository.save(testMember);
    }

    @PostMapping("/login")
    public String login(TestLoginMemberDto loginMemberDto) {
        TestMember member = testRepository.findByUsername(loginMemberDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("없는 유저네임"));

        if(!passwordEncoder.matches(loginMemberDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("패스워드 일치하지 않는다.");
        }

        return jwtTokenProvider.generateToken(member.getUsername());
    }
}