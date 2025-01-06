package com.project.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberDeleteResponseDTO {
    private String message;
    private int status;
}
