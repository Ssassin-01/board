package com.project.board.controller;

import com.project.board.dto.*;
import com.project.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> registerMember(@RequestBody UserRequestDTO userRequestDTO) {
        SignupResponseDTO response = memberService.registerMember(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = memberService.loginMember(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberDTO> getMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalStateException("인증된 사용자가 없습니다.");
        }
        String username = userDetails.getUsername();
        MemberDTO memberDto = memberService.getMemberInfo(username);
        return ResponseEntity.ok(memberDto);
    }

    @PutMapping("/update")
    public ResponseEntity<MemberUpdateResponseDTO> updateMemberInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @RequestBody MemberUpdateDTO memberUpdateDTO) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 없습니다.");
        }

        String username = userDetails.getUsername();
        MemberUpdateResponseDTO res = memberService.updateMemberInfo(username, memberUpdateDTO);

        return ResponseEntity.ok(res);
    }
}