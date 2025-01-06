package com.project.board.service;

import com.project.board.dto.*;
import com.project.board.entity.Member;
import com.project.board.repository.MemberRepository;
import com.project.board.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignupResponseDTO registerMember(UserRequestDTO userRequestDTO) {
        // 중복 사용자명 확인
        if (memberRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        // 중복 이메일 확인
        if (memberRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());

        // 사용자 저장
        Member member = Member.builder()
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                .password(encodedPassword)
                .build();

        memberRepository.save(member);

        return new SignupResponseDTO("회원가입이 성공적으로 완료되었습니다.", 201);
    }

    public LoginResponseDTO loginMember(LoginRequestDTO requestDTO) {
        Member member = memberRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(requestDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.generateToken(member.getUsername());

        return new LoginResponseDTO(token, member.getUsername(), "USER");
    }

    public MemberDTO getMemberInfo(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을 수 없습니다." + username));

        return new MemberDTO(member.getId(), member.getUsername(), member.getPassword());
    }


    public MemberUpdateResponseDTO updateMemberInfo(String username, MemberUpdateDTO updateDTO) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다." + username));

        if(updateDTO.getUsername() != null) {
            member.setUsername(updateDTO.getUsername());
        }

        if(updateDTO.getEmail() != null) {
            member.setEmail(updateDTO.getEmail());
        }

        if(updateDTO.getPassword() != null) {
            member.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        Member res = memberRepository.save(member);
        return MemberUpdateResponseDTO.builder()
                .id(res.getId())
                .username(res.getUsername())
                .email(res.getEmail())
                .message("회원정보 수정 완료")
                .build();
    }

    public MemberDeleteResponseDTO deleteMemberInfo(String username) {
        Member member =  memberRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("해당유저가 존재하지 않습니다." + username));

        memberRepository.delete(member);

        return new MemberDeleteResponseDTO("회원 삭제 성공", 200);
    }
}