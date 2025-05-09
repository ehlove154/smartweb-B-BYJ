package com.yjb.studyuserbbs.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "index")
public class ArticleEntity {
    private int index;
    private String userEmail;
    private String boardId;
    private String title;
    private String content;
    private int view;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isDeleted;
}
