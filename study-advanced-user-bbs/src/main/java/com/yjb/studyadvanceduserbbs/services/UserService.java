package com.yjb.studyadvanceduserbbs.services;

import com.yjb.studyadvanceduserbbs.entities.EmailTokenEntity;
import com.yjb.studyadvanceduserbbs.mappers.EmailTokenMapper;
import com.yjb.studyadvanceduserbbs.mappers.UserMapper;
import com.yjb.studyadvanceduserbbs.results.CommonResult;
import com.yjb.studyadvanceduserbbs.results.Result;
import com.yjb.studyadvanceduserbbs.results.ResultTuple;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final EmailTokenMapper emailTokenMapper;
    private final UserMapper userMapper;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Autowired
    public UserService(EmailTokenMapper emailTokenMapper, UserMapper userMapper, JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine) {
        this.emailTokenMapper = emailTokenMapper;
        this.userMapper = userMapper;
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
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

    public ResultTuple<EmailTokenEntity> sendRegisterEmail(String email, String userAgent) throws MessagingException {
        if (!UserService.isEmailValid(email) || userAgent == null) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }
        if (this.userMapper.selectCountByEmail(email) > 0) {
            return ResultTuple.<EmailTokenEntity>builder()
                    .result(CommonResult.FAILURE_DUPLICATE)
                    .build();
        }
        String code = RandomStringUtils.randomNumeric(6); // 인증번호 생성 ("000000" ~ "999999")
        String salt = RandomStringUtils.randomAlphanumeric(128); // salt 생성 (a~z + A~Z + 0~9)
        EmailTokenEntity emailToken = new EmailTokenEntity();
        emailToken.setEmail(email);
        emailToken.setCode(code);
        emailToken.setSalt(salt);
        emailToken.setUserAgent(userAgent);
        emailToken.setUsed(false);
        emailToken.setCreatedAt(LocalDateTime.now());
        emailToken.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        //                         ^ 현재 시간에서    ^ 10분 추가(미래로)
        // > 현재 시간보다 10분 뒤
        if (this.emailTokenMapper.insert(emailToken) < 1) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }

        // how to send the email from here
        Context context = new Context();
        context.setVariable("code", emailToken.getCode());
        String mailText = this.springTemplateEngine.process("user/registerEmail", context);
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("ehlove154@gmail.com");
        mimeMessageHelper.setTo(emailToken.getEmail());
        mimeMessageHelper.setSubject("[SAUB] 회원가입 인증번호");
        mimeMessageHelper.setText(mailText, true);
        this.javaMailSender.send(mimeMessage);
        // to here

        return ResultTuple.<EmailTokenEntity>builder()
                .result(CommonResult.SUCCESS)
                .payload(emailToken)
                .build();
    }

    public Result checkNickname(String nickname) {
        return null;
    }
}
