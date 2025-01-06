package com.project.board.test.controller;


import com.project.board.test.dto.*;
import com.project.board.test.service.MemberTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestMemberController {
    private final MemberTestService memberTestService;

    @PostMapping("/register")
    public ResponseEntity<SignupTestResponseDTO> signUp(@RequestBody SignUpTestDTO signUpTestDTO) {
        SignupTestResponseDTO response = memberTestService.signUp(signUpTestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseTestDTO> login(@RequestBody LoginRequestTestDTO loginRequestDTO) {
        LoginResponseTestDTO response = memberTestService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberTestDTO> memberInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된자가 아닙니다.");
        }
        MemberTestDTO member = memberTestService.memberTestInfo(userDetails.getUsername());
        return ResponseEntity.ok(member);
    }
}

