package com.yjb.studyadvanceduserbbs.mappers;

import com.yjb.studyadvanceduserbbs.entities.CommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {
    int insert(@Param(value = "comment") CommentEntity comment);

    CommentEntity[] selectByArticleId(@Param(value = "articleId") int articleId);
}
