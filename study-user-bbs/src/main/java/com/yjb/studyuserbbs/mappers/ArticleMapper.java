package com.yjb.studyuserbbs.mappers;

import com.yjb.studyuserbbs.entities.ArticleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
    int insert(@Param(value = "article")ArticleEntity article);

    ArticleEntity selectByIndex(@Param(value = "index")int index);

    // PK를 기준으로 PK가 아닌 모든 열의 값을 수정할 수 있도록 쿼리 작성
    int update(@Param(value = "article")ArticleEntity article);
}
