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
    public ResponseEntity<CommonResponseDTO<Void>> registerMember(@RequestBody MemberRequestDTO requestDTO) {
        CommonResponseDTO<Void> response = memberService.registerMember(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDTO<LoginResponseDTO>> login(@RequestBody MemberRequestDTO requestDTO) {
        CommonResponseDTO<LoginResponseDTO> response = memberService.loginMember(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponseDTO<MemberDTO>> getMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            System.out.println("userDetails is null!");
            throw new IllegalStateException("인증된 사용자가 없습니다.");
        }

        String username = userDetails.getUsername();
        CommonResponseDTO<MemberDTO> memberDto = memberService.getMemberInfo(username);
        return ResponseEntity.ok(memberDto);
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponseDTO<MemberDTO>> updateMemberInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @RequestBody MemberUpdateRequestDTO memberUpdateDTO) {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 없습니다.");
        }

        String username = userDetails.getUsername();
        CommonResponseDTO<MemberDTO> res = memberService.updateMemberInfo(username, memberUpdateDTO);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CommonResponseDTO<Void>> deleteMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
        String username = userDetails.getUsername();
        CommonResponseDTO<Void> res = memberService.deleteMemberInfo(username);
        return ResponseEntity.ok(res);
    }
}