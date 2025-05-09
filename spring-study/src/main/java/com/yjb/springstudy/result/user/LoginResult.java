package com.yjb.springstudy.result.user;

public enum LoginResult {
    FAILURE_COMBINATION, // 아이디 혹은 비밀번호 틀림 <아이디 혹은 비밀번호가 올바르지 않습니다.> 출력
    FAILURE_INVALID_ID, // 올바르지 않은 아이디 (아이디가 틀린게 아니라 null이거나 길이 미만/초과) <올바르지 않은 아이디 규격입니다.> 출력
    FAILURE_INVALID_PASSWORD, // 올바르지 않은 비밀번호 (비밀번호가 틀린게 아니라 null이거나 길이 미만/초과) <올바르지 않은 비밀번호 규격입니다.> 출력
    SUCCESS // 로그인 성공 <로그인 성공> 출력
}
