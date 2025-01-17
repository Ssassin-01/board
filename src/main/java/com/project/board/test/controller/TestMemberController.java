package com.project.board.test.controller;

import com.project.board.test.dto.*;
import com.project.board.test.entity.TestMember;
import com.project.board.test.repository.TestMemberRepository;
import com.project.board.test.service.TestMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestMemberController {

    private final TestMemberService testMemberService;

    @PostMapping("/create")
    public ResponseEntity<TestCommonResponseDTO> createMember(@RequestBody TestCreateMemberRequestDTO requestDTO) {
        TestCommonResponseDTO res = testMemberService.createMember(requestDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<TestMemberResponseDTO> loginMember(@RequestBody TestMemberSignUpRequestDTO requestDTO) {
        TestMemberResponseDTO res = testMemberService.loginMember(requestDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    public ResponseEntity<TestMemberInfoResponseDTO> info(@AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 유저가 아닙니다.");
        }
        TestMemberInfoResponseDTO res = testMemberService.info(userDetails.getUsername());
        return ResponseEntity.ok(res);
    }

    @PutMapping("/update")
    public ResponseEntity<TestMemberInfoResponseDTO> updateMember(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestBody TestCreateMemberRequestDTO requestDTO) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 유저가 아닙니다.");
        }
        TestMemberInfoResponseDTO res = testMemberService.updateMember(userDetails.getUsername(), requestDTO);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<TestCommonResponseDTO> deleteMember(@AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 유저가 아닙니다.");
        }
        TestCommonResponseDTO res = testMemberService.deleteMember(userDetails.getUsername());
        return ResponseEntity.ok(res);
    }

}

