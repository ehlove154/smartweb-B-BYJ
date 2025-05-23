package com.yjb.studyadvanceduserbbs.mappers;

import com.yjb.studyadvanceduserbbs.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int selectCountByEmail(@Param(value = "email") String email);

    int selectCountByNickname(@Param(value = "nickname") String nickname);

    int selectCountByContact(@Param(value = "contactFirst") String contactFirst,
                             @Param(value = "contactSecond") String contactSecond,
                             @Param(value = "contactThird")String contactThird);

    int insert(UserEntity user);

    UserEntity selectByEmail(@Param(value = "email") String email);

    int update(@Param(value = "user") UserEntity user);
}
