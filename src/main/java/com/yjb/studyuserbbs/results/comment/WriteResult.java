package com.yjb.studyuserbbs.results.comment;

public enum WriteResult {
    FAILURE, // 정규화 실패
    FAILURE_ARTICLE_ABSENT, // 게시글 없음
    FAILURE_SESSION_EXPIRED, //로그아웃 됨, 권한 없음 등
    SUCCESS
}
