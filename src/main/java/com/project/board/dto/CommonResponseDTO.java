package com.project.board.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponseDTO<T> {
    private String message;
    private int status;
    private T data;
}