package com.project.board.test.controller;

import com.project.board.test.dto.TestRequestRegisterDTO;
import com.project.board.test.dto.TestResponseRegisterDTO;
import com.project.board.test.entity.TestMember;
import com.project.board.test.repository.TestMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tests")
public class TestMemberController {
    private final TestMemberRepository testMemberRepository;
    /*
    회원가입
     username, email, password
     */

    @PostMapping("/signUp")
    public ResponseEntity<TestResponseRegisterDTO> SignUp(@RequestBody TestRequestRegisterDTO requestDTO){


        //해당 유저네임이 존재하니?
        if(testMemberRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("해당 유저는 이미 존재합니다");
        }

        //해당 이메일이 존재하니?
        if(testMemberRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("해당 이메일은 이미 존재합니다");
        }

        TestMember member = TestMember.builder()
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .username(requestDTO.getUsername())
                .build();

        testMemberRepository.save(member);

        TestResponseRegisterDTO res = TestResponseRegisterDTO.builder()
                .message("회원가입 성공")
                .status(200)
                .build();

        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public String
}
