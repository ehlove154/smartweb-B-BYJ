package com.yjb.studyadvanceduserbbs.services;

import com.yjb.studyadvanceduserbbs.entities.EmailTokenEntity;
import com.yjb.studyadvanceduserbbs.mappers.EmailTokenMapper;
import com.yjb.studyadvanceduserbbs.results.CommonResult;
import com.yjb.studyadvanceduserbbs.results.Result;
import com.yjb.studyadvanceduserbbs.results.email_token.VerifyEmailTokenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailTokenService {
    public static boolean isCodeValid(String code) {
        return code != null && code.matches("^(\\d{6})$");
    }

    public static boolean isSaltValid(String salt) {
        return salt != null && salt.matches("^([\\da-zA-Z]{128})$");
    }

    private final EmailTokenMapper emailTokenMapper;

    @Autowired
    public EmailTokenService(EmailTokenMapper emailTokenMapper) {
        this.emailTokenMapper = emailTokenMapper;
    }

    public Result verifyEmailToken(EmailTokenEntity emailToken) {
//         1. 전달 받은 emailToken 매개변수 및 이의 email, code, salt 정규화. 실패시 CommonResult.FAILURE 반환
        if (emailToken == null ||
                !UserService.isEmailValid(emailToken.getEmail()) ||
        !EmailTokenService.isCodeValid(emailToken.getCode()) ||
        !EmailTokenService.isSaltValid(emailToken.getSalt())) {
            return CommonResult.FAILURE;
        }

//         2. EmailTokenMapper의 selectByEmailAndCodeAndSalt 호출하여 EmailTokenEntity 반환받기
        EmailTokenEntity dbEmailToken = this.emailTokenMapper.selectByEmailAndCodeAndSalt(emailToken.getEmail(), emailToken.getCode(), emailToken.getSalt());

//         3. <2>가 null일 경우 CommonResult.FAILURE 반환
        if (dbEmailToken == null) {
            return CommonResult.FAILURE;
        }

//         4. <2>가 가지고 있는 userAgent가 전달 받은 emailToken의 userAgent와 다를 경우(브라우저 다름) CommonResult.FAILURE 반환
        if (!dbEmailToken.getUserAgent().equals(emailToken.getUserAgent())) {
            return CommonResult.FAILURE;
        }

//         5. <2>가 가지고 있는 isUsed가 true일 경우 CommonResult.FAILURE 반환
        if (dbEmailToken.isUsed()) {
            return CommonResult.FAILURE;
        }

//         6. <2>가 가지고 있는 expiredAt이 현재 시간 보다 과거일 경우(만료) VerifyEmailTokenResult.FAILURE_EXPIRED 반환
        if (dbEmailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return VerifyEmailTokenResult.FAILURE_EXPIRED;
        }

//         7. <2>의 isUsed를 true로 지정하고 EmailTokenMapper의 update 호출(인자는 <2>)
//         8. <7>의 결과가 0보다 크면 CommonResult.SUCCESS를, 아니면 FAILURE 반환
        dbEmailToken.setUsed(true);
        return this.emailTokenMapper.update(dbEmailToken) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }
}
