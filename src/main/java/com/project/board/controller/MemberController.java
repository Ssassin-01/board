package com.project.board.controller;

import com.project.board.dto.LoginRequestDTO;
import com.project.board.dto.MemberDTO;
import com.project.board.dto.UserRequestDTO;
import com.project.board.service.MemberService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> registerMember(@RequestBody UserRequestDTO userRequestDTO) {
        memberService.registerMember(userRequestDTO);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String token = memberService.loginMember(loginRequestDTO);
        return ResponseEntity.ok(token);
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
}