package com.yjb.studyadvanceduserbbs.services;

import com.yjb.studyadvanceduserbbs.entities.EmailTokenEntity;
import com.yjb.studyadvanceduserbbs.entities.UserEntity;
import com.yjb.studyadvanceduserbbs.mappers.EmailTokenMapper;
import com.yjb.studyadvanceduserbbs.mappers.UserMapper;
import com.yjb.studyadvanceduserbbs.results.CommonResult;
import com.yjb.studyadvanceduserbbs.results.Result;
import com.yjb.studyadvanceduserbbs.results.ResultTuple;
import com.yjb.studyadvanceduserbbs.results.user.LoginResult;
import com.yjb.studyadvanceduserbbs.results.user.RegisterResult;
import com.yjb.studyadvanceduserbbs.utils.CryptoUtils;
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

    private static EmailTokenEntity generateEmailToken(String email, String userAgent, int expMin) {
        String code = RandomStringUtils.randomNumeric(6); // 인증번호 생성 ("000000" ~ "999999")
        String salt = RandomStringUtils.randomAlphanumeric(128); // salt 생성 (a~z + A~Z + 0~9)
        return UserService.generateEmailToken(email, userAgent, code, salt, expMin);
    }

    private static EmailTokenEntity generateEmailToken(String email, String userAgent, String code, String salt, int expMin) {
        EmailTokenEntity emailToken = new EmailTokenEntity();
        emailToken.setEmail(email);
        emailToken.setCode(code);
        emailToken.setSalt(salt);
        emailToken.setUserAgent(userAgent);
        emailToken.setUsed(false);
        emailToken.setCreatedAt(LocalDateTime.now());
        emailToken.setExpiresAt(LocalDateTime.now().plusMinutes(expMin));
        //                         ^ 현재 시간에서    ^ 10분 추가(미래로)
        // > 현재 시간보다 10분 뒤
        return emailToken;
    }

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

    public static boolean isNameValid(String input) {
        return input != null && input.matches("^([가-힣]{2,5})$");
    }

    public static boolean isContactSecondValid(String input) {
        return input != null && input.matches("^(\\d{3,4})$");
    }

    public static boolean isContactThirdValid(String input) {
        return input != null && input.matches("^(\\d{4})$");
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
        EmailTokenEntity emailToken = UserService.generateEmailToken(email, userAgent, 10);

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
        if (!UserService.isNicknameValid(nickname)) {
            return CommonResult.FAILURE;
        }
        return this.userMapper.selectCountByNickname(nickname) > 0 ? CommonResult.FAILURE : CommonResult.SUCCESS;
    }

    public Result register(EmailTokenEntity emailToken, UserEntity user) {
        // 1. 전달 받은 emailToken 및 user 에 대한 정규화 실시. 실패시 CommonResult.FAILURE
        if (emailToken == null ||
                user == null ||
                user.getBirth() == null ||
                user.getGender() == null ||
                (!user.getGender().equals("M") && !user.getGender().equals("F")) ||
                user.getContactMvnoCode() == null ||
                user.getContactFirst() == null ||
                user.getAddressPostal() == null ||
                user.getAddressPrimary() == null ||
                user.getAddressSecondary() == null ||
                !UserService.isEmailValid(emailToken.getEmail()) ||
                !EmailTokenService.isCodeValid(emailToken.getCode()) ||
                !EmailTokenService.isSaltValid(emailToken.getSalt()) ||
                !UserService.isEmailValid(user.getEmail()) ||
                !UserService.isNicknameValid(user.getNickname()) ||
                !UserService.isPasswordValid(user.getPassword()) ||
                !UserService.isNameValid(user.getName()) ||
                !UserService.isContactSecondValid(user.getContactSecond()) ||
                !UserService.isContactThirdValid(user.getContactThird())) {
            return CommonResult.FAILURE;
        }

        // 2. 의존성 emailTokenMapper를 통해 전달 받은 emailToken이 가지는 email, code, salt를 기준으로 EmailTokenEntity SELECT
        EmailTokenEntity dbEmailToken = this.emailTokenMapper.selectByEmailAndCodeAndSalt(
                emailToken.getEmail(),
                emailToken.getCode(),
                emailToken.getSalt());

        // 3. 아래의 경우 중 한가지라도 만족 한다면, 이메일 인증이 정상적으로 이루어 지지 않은 것 임으로 CommonResult.FAILURE
        //      - <2>가 null일 경우
        //      - <2>의 isUsed가 false
        //      - <2>의 userAgent가  전달받은 emailToken의 userAgent 값과 다를 경우
        if (dbEmailToken == null || !dbEmailToken.isUsed() || !dbEmailToken.getUserAgent().equals(emailToken.getUserAgent())) {
            return CommonResult.FAILURE;
        }

        // 4. 의존성 userMapper를 통해 전달 받은 user가 가지는 email을 전달, 그 결과가 0보다 크다면 RegisterResult.FAILURE_DUPLICATE_EMAIL 반환.
        if (this.userMapper.selectCountByEmail(user.getEmail()) > 0) {
            return RegisterResult.FAILURE_DUPLICATE_EMAIL;
        }

        // 5. 의존성 userMapper를 통해 전달 받은 user가 가지는 nickname을 전달, 그 결과가 0보다 크다면 RegisterResult.FAILURE_DUPLICATE_NICKNAME 반환.
        if (userMapper.selectCountByNickname(user.getNickname()) > 0) {
            return RegisterResult.FAILURE_DUPLICATE_NICKNAME;
        }

        // 6. 의존성 userMapper를 통해 전달 받은 user가 가지는 contact를 전달, 그 결과가 0보다 크다면 RegisterResuLt.FAILURE_DUPLICATE_CONTACT 반환.
        if (userMapper.selectCountByContact(user.getContactFirst(), user.getContactSecond(), user.getContactThird()) > 0) {
            return RegisterResult.FAILURE_DUPLICATE_CONTACT;
        }

        // 7. GryptoUtils의 hashSha512를 통해 전달 받은 user가 가지는 password를 해시 암호화.
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));

        // 8. 전달 받은 user의 아래 각 멤버 변수를 해당하는 값으로 할당.
        //      - LastSignedAt: 현재 일시
        //      - lastSignedUa: 전달 받은 emailToken가 가지는 userAgent
        //      - isAdmin/isDeleted/isSuspended: false
        //      - createdAt/modifiedAt: 현재 일시
        user.setLastSignedAt(LocalDateTime.now());
        user.setLastSignedUa(emailToken.getUserAgent());

        user.setAdmin(false);
        user.setDeleted(false);
        user.setSuspended(false);

        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());

        // 9. 의존성 userMapper를 통해 전달 받은 user를 INSERT하고 그 결과 값이 0보다 클 경우 CommonResult. SUCCESS를, 아닐 경우 ConmonResult.FAILURE를 반환.
        return this.userMapper.insert(user) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public ResultTuple<UserEntity> login(String email, String password) {

        // 1. 전달 받은 email과 password에 대한 정규화. 실패시 result로 CommonResult.FAILURE를 가지는 ResultTuple 반환
        if (email == null ||
                password == null ||
                !UserService.isEmailValid(email) ||
                !UserService.isPasswordValid(password)) {
            return ResultTuple.<UserEntity>builder().result(CommonResult.FAILURE).build();
        }

        // 2. 의존성 userMapper의 seletByEmail을 통해 전달 받은 email을 전달하여 UserEntity SELECT
        UserEntity dbUser = this.userMapper.selectByEmail(email);

        // 3. <2>가 null이거나 isDeleted가 true일 경우 result로 CommonResult.FAILURE를 가지는 ResultTuple 반환
        if (dbUser == null || dbUser.isDeleted()) {
            return ResultTuple.<UserEntity>builder().result(CommonResult.FAILURE).build();
        }

        // 4. 전달 받은 password를 SHA-512로 해싱하여 <2>가 가지는 password와 비교하여 일치하지 않을 경우 result로 CommonResult.FAILURE를 가지는 ResultTuple 반환
        String hashedPassword = CryptoUtils.hashSha512(password);
        if (!hashedPassword.equals(dbUser.getPassword())) {
            return ResultTuple.<UserEntity>builder().result(CommonResult.FAILURE).build();
        }

        // 5. <2>가 가지는 isSuspended가 true일 경우 result로 LoginResult.FAILURE_SUSPENDED를 가지는 ResultTuple 반환
        if (dbUser.isSuspended()) {
            return ResultTuple.<UserEntity>builder().result(LoginResult.FAILURE_SUSPENDED).build();
        }

        // 6. result로 CommonResult.SUCCESS를, payload로 <2>를 가지는 ResultTuple 반환.
        return ResultTuple.<UserEntity>builder().result(CommonResult.SUCCESS).payload(dbUser).build();
    }

    public ResultTuple<EmailTokenEntity> sendRecoverPasswordEmail(String email, String userAgent) throws MessagingException {
        if (!UserService.isEmailValid(email) || userAgent == null) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }
        UserEntity dbUser = this.userMapper.selectByEmail(email);
        if (dbUser == null || dbUser.isDeleted()) {
            return ResultTuple.<EmailTokenEntity>builder()
                    .result(CommonResult.FAILURE_ABSENT)
                    .build();
        }

        EmailTokenEntity emailToken = UserService.generateEmailToken(email, userAgent, 10);
        if (this.emailTokenMapper.insert(emailToken) < 1) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }

        // how to send the email from here
        Context context = new Context();
        context.setVariable("code", emailToken.getCode());
        String mailText = this.springTemplateEngine.process("user/recoverPasswordEmail", context);
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("ehlove154@gmail.com");
        mimeMessageHelper.setTo(emailToken.getEmail());
        mimeMessageHelper.setSubject("[SAUB] 비밀번호 재설정 인증번호");
        mimeMessageHelper.setText(mailText, true);
        this.javaMailSender.send(mimeMessage);
        // to here

        return ResultTuple.<EmailTokenEntity>builder()
                .result(CommonResult.SUCCESS)
                .payload(emailToken)
                .build();
    }

    public Result recoverPassword(EmailTokenEntity emailToken, String newPassword) {
        // 1. emailToken 및 emailToken이 가지고 있는 email, code, salt에 대해 정규화. 실패시 CommonResult.FAILURE
        if (emailToken == null ||
                emailToken.getEmail() == null || emailToken.getEmail().isEmpty() ||
                emailToken.getCode() == null || emailToken.getCode().isEmpty() ||
                emailToken.getSalt() == null || emailToken.getSalt().isEmpty()) {
            return CommonResult.FAILURE;
        }

        // 2. newPassword에 대해 정규화. 실패시 실패시 CommonResult.FAILURE
        if (!UserService.isPasswordValid(newPassword)) {
            return CommonResult.FAILURE;
        }

        // 3. 주입된 의존성 emailTokenMapper의 selectByEmailAndCodeAndSalt를 호출하여 db에 저장되어 있는 EmailTokenEntity 반환
        EmailTokenEntity dbTokenEntity = this.emailTokenMapper.selectByEmailAndCodeAndSalt(emailToken.getEmail(), emailToken.getCode(), emailToken.getSalt());

        // 4. 아래 항목 중 하나라도 만족 할 경우 CommonResult.FAILURE
        //      - <3>이 null
        //      - <3>이 가지고 있는 userAgent가 매개변수인 emailToken의 userAgent와 일치하지 않음
        //      - <3>이 가지고 있는 isUsed가 false
        if (dbTokenEntity == null ||
                !dbTokenEntity.getUserAgent().equals(emailToken.getUserAgent()) ||
                !dbTokenEntity.isUsed()) {
            return CommonResult.FAILURE;
        }

        // 5. 매개 변수인 emailToken이 가지고 있는 email을 전달 인자로 전달하여 주입된 의존성 userMapper의 selectByEmail 호출
        UserEntity dbUser = this.userMapper.selectByEmail(emailToken.getEmail());

        // 6. 아래 항목 중 하나라도 만족할 경우 CommonResult.FAILURE
        //      - <5>가 null
        //      - <5>가 가지고 있는 isDeleted 혹은 isSuspended가 true
        if (dbUser == null || dbUser.isDeleted() || dbUser.isSuspended()) {
            return CommonResult.FAILURE;
        }

        // 7. 매개 변수인 newPassword를 SHA-512 알고리즘을 통해 해싱
        newPassword = CryptoUtils.hashSha512(newPassword);

        // 8. <6>이 가지고 있는 password를 <7>로 교체하고 주입된 의존성 userMapper의 update의 인자로 전달하여 호출
        dbUser.setPassword(newPassword);

        // 9. <8>의 결과가 0보다 클 경우 CommonResult.SUCCESS를, 아닐 경우 CommonResult.FAILURE를 반환
        return this.userMapper.update(dbUser) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }
}
