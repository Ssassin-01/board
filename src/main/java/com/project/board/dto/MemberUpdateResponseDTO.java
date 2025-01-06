package com.project.board.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String message;
}
