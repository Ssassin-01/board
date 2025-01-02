package com.project.board.test.controller;

import com.project.board.test.dto.TestLoginMemberDto;
import com.project.board.test.dto.TestRegisterMemberDto;
import com.project.board.test.service.TestMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tests")
public class TestMemberController {
    private final TestMemberService testMemberService;

    @PostMapping("/registerTest")
    public ResponseEntity<String> register(@RequestBody TestRegisterMemberDto memberDto) {
        testMemberService.registerMember(memberDto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody TestLoginMemberDto loginMemberDto) {
        String token = testMemberService.login(loginMemberDto);
        return ResponseEntity.ok(token);
    }
}