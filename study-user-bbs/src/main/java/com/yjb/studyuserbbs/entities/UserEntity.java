package com.yjb.studyuserbbs.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 열 이름 -> 카멜케이스
// date -> localdate
// datetime -> localdatetime

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "email")
public class UserEntity {
    private String email;
    private String password;
    private String nickname;
    private LocalDate birthday;
    private LocalDate termAgreedAt;
    private LocalDate marketingAgreedAt;
    private boolean isAdmin;
    private boolean isDeleted;
    private boolean isSuspended;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
