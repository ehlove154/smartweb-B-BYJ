package com.yjb.springstudy.results.calc;

public enum CalculateResult {
    FAILURE_DIVIDE_BY_ZERO, // 0으로 나눌 수 없음
    FAILURE_INVALID_NUMBER, // 올바른 숫자가 아님
    FAILURE_INVALID_OPERATOR, // 올바른 연산자 아님
    SUCCESS // 성공
}
