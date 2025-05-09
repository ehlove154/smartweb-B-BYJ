package com.yjb.studyuserbbs.services;

import com.yjb.studyuserbbs.entities.UserEntity;
import com.yjb.studyuserbbs.mappers.UserMapper;
import com.yjb.studyuserbbs.results.user.*;
import com.yjb.studyuserbbs.utils.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
//@RequiredArgsConstructor

public class UserService {
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
    private final UserMapper userMapper;

    @Autowired /* => @RequiredArgsConstructor */
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public LoginResult login(UserEntity user) {
        if (user == null ||
                !UserService.isEmailValid(user.getEmail()) ||
                !UserService.isPasswordValid(user.getPassword())) {
            return LoginResult.FAILURE;
        }
        UserEntity dbUser = this.userMapper.selectByEmail(user.getEmail());
        if (dbUser == null) {
            return LoginResult.FAILURE;
        }
        if (dbUser.isDeleted()) {
            return LoginResult.FAILURE;
        }
        String hashedPassword = CryptoUtils.hashSha512(user.getPassword());
        if (!dbUser.getPassword().equals(hashedPassword)) {
            return LoginResult.FAILURE;
        }
        if (dbUser.isSuspended()) {
            return LoginResult.FAILURE_SUSPENDED;
        }
//        user = dbUser;
        user.setPassword(dbUser.getPassword());
        user.setNickname(dbUser.getNickname());
        user.setBirthday(dbUser.getBirthday());
        user.setTermAgreedAt(dbUser.getTermAgreedAt());
        user.setMarketingAgreedAt(dbUser.getMarketingAgreedAt());
        user.setCreatedAt(dbUser.getCreatedAt());
        user.setModifiedAt(dbUser.getModifiedAt());
        user.setAdmin(dbUser.isAdmin());
        user.setDeleted(dbUser.isDeleted());
        user.setSuspended(dbUser.isSuspended());
        return LoginResult.SUCCESS;
    }

    public ModifyResult modify (UserEntity signedUser, String currentPassword, String newPassword, String newNickname, String newBirthday) {
        if (signedUser == null || signedUser.isDeleted() || signedUser.isSuspended()) {
            return ModifyResult.FAILURE_SESSION_EXPIRED;
        }
        if (!UserService.isPasswordValid(currentPassword)) {
            System.out.println(1);
            return ModifyResult.FAILURE;
        }
        String hashedCurrentPassword = CryptoUtils.hashSha512(currentPassword);
        if (!signedUser.getPassword().equals(hashedCurrentPassword)) {
            return ModifyResult.FAILURE_PASSWORD_MISMATCH;
        }
        String hashedNewPassword = null;
        if (newPassword != null) {
            if (!UserService.isPasswordValid(newPassword)) {
                System.out.println(2);
                return ModifyResult.FAILURE;
            }
            hashedNewPassword = CryptoUtils.hashSha512(newPassword);
            if (signedUser.getPassword().equals(hashedNewPassword)) {
                return ModifyResult.FAILURE_PASSWORD_SAME;
            }
        }
        if (!UserService.isNicknameValid(newNickname)) {
            System.out.println(3);
            return ModifyResult.FAILURE;
        }
        if (signedUser.getNickname().equals(newNickname) && this.userMapper.selectCountByNickname(newNickname) > 0) {
            return ModifyResult.FAILURE_NICKNAME_NOT_AVAILABLE;
        }
        if (newBirthday != null && !UserService.isBirthValid(newBirthday)) {
            System.out.println(4);
            return ModifyResult.FAILURE;
        }
        LocalDate newBirthInstance = newBirthday == null
                ? null
                : LocalDate.parse(newBirthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        UserEntity user = new UserEntity();
        user.setEmail(signedUser.getEmail());
        user.setPassword(hashedNewPassword == null ? signedUser.getPassword() : hashedNewPassword);
        user.setNickname(newNickname);
        user.setBirthday(newBirthInstance);
        user.setTermAgreedAt(signedUser.getTermAgreedAt());
        user.setMarketingAgreedAt(signedUser.getMarketingAgreedAt());
        user.setCreatedAt(signedUser.getCreatedAt());
        user.setAdmin(signedUser.isAdmin());
        user.setDeleted(signedUser.isDeleted());
        user.setSuspended(signedUser.isSuspended());
        user.setModifiedAt(LocalDateTime.now());
        if (this.userMapper.update(user) > 0) {
            signedUser.setPassword(user.getPassword());
            signedUser.setNickname(user.getNickname());
            signedUser.setBirthday(user.getBirthday());
            signedUser.setModifiedAt(user.getModifiedAt());
            return ModifyResult.SUCCESS;
        } else {
            return ModifyResult.FAILURE;
        }
    }

    public RecoverEmailResult recoverEmail(UserEntity user) {
        // 1. 전달받은 user가 null이거나, 이가 가진 nickname이 올바르지 않을 경우 FAILURE
        if (user == null || !UserService.isNicknameValid(user.getNickname())) {
            return RecoverEmailResult.FAILURE;
        }
        // 2. UserMapper가 가진 selectByNickname을 통해 dbUser반환 받기
        UserEntity dbUser = this.userMapper.selectByNickname(user.getNickname());
        // 3. <2>에서 반환된 UserEntity가 null이거나 isDeleted()가 true면 FAILURE
        if (dbUser == null || dbUser.isDeleted()) {
            return RecoverEmailResult.FAILURE;
        }
        // 4. 객체 개념을 활용하여 반환 타입은 RecoverEmailResult를 유지하고, dbUser의 이메일을 컨트롤러에게 넘겨주기. SUCCESS
        user.setEmail(dbUser.getEmail());
        return RecoverEmailResult.SUCCESS;
    }

    public RecoverPasswordResult recoverPassword(UserEntity user) {
        // 1. 전달받은 user가 null이거나, 이가 가진 email, nickname, password 중 하나라도 올바르지 않은 값이 있을 경우 FAILURE
        if (user == null ||
                !UserService.isEmailValid(user.getEmail()) ||
                !UserService.isNicknameValid(user.getNickname()) ||
                !UserService.isPasswordValid(user.getPassword())) {
            return RecoverPasswordResult.FAILURE;
        }
        // 2. UserMapper가 가진 selectByEmail을 통해 dbUser 반환 받기
        UserEntity dbUser = this.userMapper.selectByEmail(user.getEmail());
        // 3. <2>에서 반환된 UserEntity가 null이거나 isDeleted()가 true면 FAILURE
        if (dbUser == null || dbUser.isDeleted()) {
            return RecoverPasswordResult.FAILURE;
        }
        // 4. <2>에서 반환된 UserEntity가 가진 nickname 값이 매개변수인 user의 nickname 값과 다르다면 FAILURE
        if (!user.getNickname().equals(dbUser.getNickname())) {
            return RecoverPasswordResult.FAILURE;
        }
        dbUser.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        dbUser.setModifiedAt(LocalDateTime.now());
        // 5. <2>에서 반환된 UserEntity 객체의 password 값을 매개변수인 user의 password 값을 해싱한 것으로 대체하고 UserMapper가 가진 update 인자로 넘겨 호출하기.
        // 6. <5>의 결과가 0보다 크다면 SUCCESS, 아니라면 FAILURE
        return this.userMapper.update(dbUser) > 0 ? RecoverPasswordResult.SUCCESS : RecoverPasswordResult.FAILURE;
    }

    public RegisterResult register(UserEntity user, boolean isMarketingChecked) {

        /* RegisterResult
         * FAILURE
         * FAILURE_EMAIL_NOT_AVAILABLE
         * FAILURE_NICKNAME_NOT_AVAILABLE
         * SUCCESS
         */

        if (user == null ||
                !UserService.isEmailValid(user.getEmail()) ||
                !UserService.isPasswordValid(user.getPassword()) ||
                !UserService.isNicknameValid(user.getNickname())) {
            return RegisterResult.FAILURE;
        }
        if (this.userMapper.selectCountByEmail(user.getEmail()) > 0) {
            return RegisterResult.FAILURE_EMAIL_NOT_AVAILABLE;
        }
        if (this.userMapper.selectCountByNickname(user.getNickname()) > 0) {
            return RegisterResult.FAILURE_NICKNAME_NOT_AVAILABLE;
        }
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        user.setTermAgreedAt(LocalDate.now()); // LocalDate.now() : 현재 "날짜"를 반환하는 정적 메서드
        user.setCreatedAt(LocalDateTime.now()); // LocalDateTime.now() : 현재 "날짜/시간"을 반환하는 정적 메서드
        user.setModifiedAt(LocalDateTime.now());
        if (isMarketingChecked) {
            user.setMarketingAgreedAt(LocalDate.now());
        } else {
            user.setMarketingAgreedAt(null);
        } /* => user.setMarketingAgreedAt(isMarketingChecked ? LocalDate.now() : null); */
        return this.userMapper.insertUser(user) > 0 ? RegisterResult.SUCCESS : RegisterResult.FAILURE;
    }

    public boolean isEmailAvailable(String email) {
        if (!UserService.isEmailValid(email)) {
            return false;
        }
        return this.userMapper.selectCountByEmail(email) == 0;
    }

    public boolean isNicknameAvailable(String nickname) {
        if (!UserService.isNicknameValid(nickname)) {
            return false;
        }
        return this.userMapper.selectCountByNickname(nickname) == 0;
    }
}
