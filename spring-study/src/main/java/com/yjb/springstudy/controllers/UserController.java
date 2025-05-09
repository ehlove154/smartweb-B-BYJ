package com.yjb.springstudy.controllers;

import com.yjb.springstudy.result.user.LoginResult;
import com.yjb.springstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getLogin() {
        return "user/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String postLogin(@RequestParam(value = "id") String id,
                            @RequestParam(value = "password") String password,
                            Model model) {
        LoginResult result = this.userService.login(id, password);
        model.addAttribute("result", result.toString());
        /* 위와 같이 돌려받은 열거형에 toString() 호출하면 열거형 이름을 문자열로 돌려줌
        * 가령 돌려 받은 result가 SUCCESS 였다면 이에 대한 toString() 결과는 "SUCCESS"가 됨 */
        return "user/login";
    }
}
