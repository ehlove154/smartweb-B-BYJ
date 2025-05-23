package com.yjb.studyadvanceduserbbs.entities;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class ImageEntity {
    private int id;
    private String name;
    private String contentType;
    private byte[] data;
    private LocalDateTime createdAt;
}
