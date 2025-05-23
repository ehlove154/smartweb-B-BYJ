package com.yjb.studyadvanceduserbbs.entities;

import lombok.*;

@Builder
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
