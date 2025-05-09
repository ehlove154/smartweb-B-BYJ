package com.yjb.studyuserbbs.results.comment;

public enum DeleteResult {
    FAILURE, // 정규화 실패
    FAILURE_ABSENT, // 댓글 없음
    FAILURE_SESSION_EXPIRED, //로그아웃 됨, 권한 없음 등
    SUCCESS
}
