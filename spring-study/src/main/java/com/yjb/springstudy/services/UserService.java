package com.yjb.springstudy.services;

import com.yjb.springstudy.result.user.LoginResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private static final List<String> registeredIds;
    private static final List<String> registeredPasswords;

    static {
        registeredIds = List.of("gpcr4ww2", "q8egpa", "zdz8qr6dw2nj");
        registeredPasswords = List.of("nawhd2fw", "wfe57sfe3", "4185gewef3");
    }

    public LoginResult login(String id, String password) {
        /* 1. 전달 받은 id가 null이거나 길이가 6자 미만 혹은 15자 초과면 FAILURE_INVALID_ID 반환
        * 2. 전달 받은 password가 null이거나 길이가 6자 미만 혹은 25자 초과면 FAILURE_INVALID_PASSWORD 반환
        * 3. 전달 받은 id가 registeredIds에 없으면 FAILURE_COMBINATION 반환
        * 4.전달 받은 id를 registeredIds에서 찾아 인덱스 확보
        * 5. registeredPasswords에 <4>에서 구현한 인덱스를 대입하여 전달 받은 password와 비교 했을때, 다르면 FAILURE_COMBINATION반환, 같으면 SUCCESS 반환 */

        if (id == null || id.length() < 6 || id.length() > 15) {
            return LoginResult.FAILURE_INVALID_ID;
        }
        if (password == null || password.length() < 6 || password.length() > 25) {
            return LoginResult.FAILURE_INVALID_PASSWORD;
        }

        int index = registeredIds.indexOf(id);
        if (index == -1) {
            return LoginResult.FAILURE_COMBINATION;
        }

        if (!Objects.equals(registeredPasswords.get(index), password)) {
            return LoginResult.FAILURE_COMBINATION;
        }
        return LoginResult.SUCCESS;
    }
}
