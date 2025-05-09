package com.yjb.studymemo.entities;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "index")
public class MemoEntity {
    private int index;
    private String writer;
    private String content;
    private LocalDateTime createdAt;
}
