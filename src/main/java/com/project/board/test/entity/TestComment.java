package com.project.board.test.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(nullable = false)
    private TestMember author;

    @ManyToOne
    @JoinColumn(nullable = false)
    private TestPost post;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;
}
