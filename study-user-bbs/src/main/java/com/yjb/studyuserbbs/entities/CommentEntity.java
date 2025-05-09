package com.yjb.studyuserbbs.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "index")
public class CommentEntity {
    private int index;
    private String userEmail;
    private int articleIndex;
    private String content;
    private Integer commentIndex; // null값 허용을 위해 Integer 사용
    private LocalDateTime createAt;
    private boolean isDeleted;

}
