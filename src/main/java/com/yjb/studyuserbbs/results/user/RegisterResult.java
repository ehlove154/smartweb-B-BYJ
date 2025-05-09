package com.yjb.studyuserbbs.results.user;

public enum RegisterResult {
    FAILURE, // 실패
    FAILURE_EMAIL_NOT_AVAILABLE, // 이메일 중복
    FAILURE_NICKNAME_NOT_AVAILABLE, // 닉네임 중복
    SUCCESS // 성공
}
