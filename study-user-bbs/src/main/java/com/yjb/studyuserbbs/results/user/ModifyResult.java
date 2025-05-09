package com.yjb.studyuserbbs.results.user;

public enum ModifyResult {
    FAILURE,
    FAILURE_NICKNAME_NOT_AVAILABLE, //닉네임 못씀(중복)
    FAILURE_PASSWORD_MISMATCH, //비밀번호 틀림
    FAILURE_PASSWORD_SAME, //비밀번호 다름
    FAILURE_SESSION_EXPIRED, //세션 만료
    SUCCESS
}
