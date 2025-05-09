package com.yjb.studyadvanceduserbbs.services;

import com.yjb.studyadvanceduserbbs.entities.EmailTokenEntity;
import com.yjb.studyadvanceduserbbs.mappers.UserMapper;
import com.yjb.studyadvanceduserbbs.results.CommonResult;
import com.yjb.studyadvanceduserbbs.results.ResultTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public static boolean isEmailValid(String input) {
        return input != null && input.matches("^(?=.{8,50}$)([\\da-z\\-_.]{4,})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2,3})?$");
    }

    public static boolean isPasswordValid(String input) {
        return input != null && input.matches("^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,50})$");
    }

    public static boolean isNicknameValid(String input) {
        return input != null && input.matches("^([\\da-zA-Z가-힣]{2,10})$");
    }

    public static boolean isBirthValid(String input) {
        return input != null && input.matches("^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$");
    }

    public ResultTuple<EmailTokenEntity> sendRegisterEmail(String email, String userAgent) {
        if (!UserService.isEmailValid(email) || userAgent == null) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }
        if (this.userMapper.selectCountByEmail(email) > 0) {
            return ResultTuple.<EmailTokenEntity>builder()
                    .result(CommonResult.FAILURE_DUPLICATE)
                    .build();
        }

        return null;
    }
}
