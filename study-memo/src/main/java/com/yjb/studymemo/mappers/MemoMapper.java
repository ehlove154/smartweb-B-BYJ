package com.yjb.studymemo.mappers;

import com.yjb.studymemo.entities.MemoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemoMapper {
    int deleteByIndex(@Param(value = "index") int index);

    // int인 이유 => insert시 영향을 받는 index의 수를 표현
    int insert(@Param(value = "writer") String writer,
               @Param(value = "content") String content);

    MemoEntity[] selectAll();
}
