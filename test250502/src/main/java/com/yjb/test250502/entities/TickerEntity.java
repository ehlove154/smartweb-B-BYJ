package com.yjb.test250502.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class TickerEntity {
    private String id;
    private String name;
}
