package com.yjb.studyuserbbs.mappers;

import com.yjb.studyuserbbs.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insertUser(@Param(value = "user") UserEntity user);

    UserEntity selectByEmail(@Param(value = "email") String email);

    UserEntity selectByNickname(@Param(value = "nickname") String nickname);

    int selectCountByEmail(@Param(value = "email") String email);

    int selectCountByNickname(@Param(value = "nickname") String nickname);

    int update(@Param(value = "user") UserEntity user);
}