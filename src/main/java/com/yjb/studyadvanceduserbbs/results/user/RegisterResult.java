package com.yjb.studyadvanceduserbbs.results.user;

import com.yjb.studyadvanceduserbbs.results.Result;

public enum RegisterResult implements Result {
    FAILURE_DUPLICATE_EMAIL,
    FAILURE_DUPLICATE_NICKNAME,
    FAILURE_DUPLICATE_CONTACT
}
