package com.yjb.studyadvanceduserbbs.entities;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class CommentEntity {
    private int id;
    private int articleId;
    private String userEmail;
    private Integer commentId;
    private String content;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}
