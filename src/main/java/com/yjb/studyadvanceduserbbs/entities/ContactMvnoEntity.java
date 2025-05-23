package com.yjb.studyadvanceduserbbs.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "code")
public class ContactMvnoEntity {
    private String code;
    private String displayText;
}
