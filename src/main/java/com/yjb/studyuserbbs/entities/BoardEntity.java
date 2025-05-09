package com.yjb.studyuserbbs.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class BoardEntity {
    private String id;
    private String displayText;
    private boolean isAdminOnly;
}