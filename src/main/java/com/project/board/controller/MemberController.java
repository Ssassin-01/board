package com.project.board.controller;

import com.project.board.dto.*;
import com.project.board.service.MemberService;
import com.project.board.service.ProfileImageService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ProfileImageService profileImageService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDTO<Void>> registerMember(@RequestBody MemberRequestDTO requestDTO) {
        CommonResponseDTO<Void> response = memberService.registerMember(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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




    @PostMapping("/profile-image")
    public ResponseEntity<Map<String, String>> uploadProfileImage(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestParam("file")MultipartFile file) throws IOException {
        if(userDetails == null) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        Long memberId = memberService.getMemberIdByUsername(userDetails.getUsername());
        String imageURL = profileImageService.uploadProfileImage(memberId, file);

        return ResponseEntity.ok(Map.of("profileImageURL", imageURL));
    }

    @GetMapping("/profile-image/{filename}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("uploads", filename).normalize();
        File file = filePath.toFile();

        if(!file.exists() || !Files.probeContentType(filePath).startsWith("image")) {
            throw new IllegalArgumentException("유효하지 않은 이미지 입니다.");
        }

        Resource resource = new UrlResource(file.toURI());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/profile-image")
    public ResponseEntity<Map<String, String>> deleteProfileImage(@AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if(userDetails == null) {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
        Long memberId = memberService.getMemberIdByUsername(userDetails.getUsername());
        profileImageService.deleteProfileImage(memberId);

        return ResponseEntity.ok(Map.of("message", "프로필 사진이 삭제되었습니다."));
    }
}