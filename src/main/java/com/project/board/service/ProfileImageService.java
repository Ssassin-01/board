package com.project.board.service;

import com.project.board.entity.Member;
import com.project.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImageService {

    private final MemberRepository memberRepository;
    private static final String UPLOAD_DIR = "uploads";
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    public String uploadProfileImage(Long memberId, MultipartFile file) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        // 기존 프로필 이미지 삭제 (수정 시)
        if (member.getProfileImageURL() != null) {
            deleteProfileImage(memberId);
        }

        // uploads 파일 존재안하면
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("✅ 'uploads' 폴더가 자동으로 생성되었습니다: {}", uploadPath);
        }

        // 파일명 체크
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.isEmpty()) {
            throw new IllegalArgumentException("파일명이 비어있습니다.");
        }

        // 지원형식 체크
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        if (fileExtension == null || !ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
        }

        // 파일 저장
        String filename = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = uploadPath.resolve(filename);

        if (!Files.probeContentType(filePath).startsWith("image")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        file.transferTo(filePath.toFile());
        log.info("파일이 저장되었습니다: {}", filePath);

        //사용자 프로필 이미지 경로 저장
        String imageURL = "/api/members/profile-image/" + filename;
        member.setProfileImageURL(imageURL);
        memberRepository.save(member);

        return imageURL;
    }

    public void deleteProfileImage(Long memberId) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        String imageURL = member.getProfileImageURL();
        if (imageURL != null) {
            String filename = imageURL.replace("/api/members/profile-image/", "");
            Path filePath = Paths.get(UPLOAD_DIR, filename).normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("🗑기존 프로필 이미지 삭제: {}", filePath);
            } else {
                log.warn("삭제할 파일이 존재하지 않습니다: {}", filePath);
            }
            member.setProfileImageURL(null);
            memberRepository.save(member);
        }
    }
}
