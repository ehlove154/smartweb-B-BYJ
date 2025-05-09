package com.yjb.studyadvanceduserbbs.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int selectCountByEmail(@Param(value = "email") String email);
}
