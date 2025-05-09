package com.yjb.studyuserbbs.mappers;

import com.yjb.studyuserbbs.entities.BoardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardMapper {
    BoardEntity selectById(@Param(value = "id") String id);
}
