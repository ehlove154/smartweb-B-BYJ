package com.yjb.studyadvanceduserbbs.mappers;

import com.yjb.studyadvanceduserbbs.entities.ArticleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
    int insert(@Param(value = "article")ArticleEntity article);

    ArticleEntity selectById(@Param(value = "id")int id);

    int update(@Param(value = "article")ArticleEntity article);

    ArticleEntity selectPrevious(@Param(value = "id")int id);

    ArticleEntity selectNext(@Param(value = "id")int id);
}
