package com.yjb.studyuserbbs.mappers;

import com.yjb.studyuserbbs.entities.CommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {
    int insert(@Param(value = "comment") CommentEntity comment);

    CommentEntity selectByIndex(@Param(value = "index")int index);

    CommentEntity selectByArticleIndex(@Param(value = "articleIndex") int articleIndex);

    int update(@Param(value = "comment") CommentEntity comment);
}
