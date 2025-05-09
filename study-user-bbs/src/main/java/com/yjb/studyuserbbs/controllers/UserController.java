package com.yjb.studyuserbbs.controllers;


import com.yjb.studyuserbbs.entities.UserEntity;
import com.yjb.studyuserbbs.mappers.UserMapper;
import com.yjb.studyuserbbs.results.user.LoginResult;
import com.yjb.studyuserbbs.results.user.ModifyResult;
import com.yjb.studyuserbbs.results.user.RegisterResult;
import com.yjb.studyuserbbs.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/email-check", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getEmailCheck(@RequestParam(value = "email")String email) {
        boolean result = this.userService.isEmailAvailable(email);
        JSONObject response = new JSONObject();
        response.put("result", result);
        return response.toString();
    }

    @RequestMapping(value = "/nickname-check", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getNicknameCheck(@RequestParam(value = "nickname")String nickname) {
        boolean result = this.userService.isNicknameAvailable(nickname);
        JSONObject response = new JSONObject();
        response.put("result", result);
        return response.toString();
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getInfo(@SessionAttribute(value =  "signedUser", required = false) UserEntity user) {
        /* 로그인이 된 상태라면 user/info 를 뷰로,
        * 로그인이 안 된 상태라면 /user/login 으로 리디렉션 */
//        if (session.getAttribute("signedUser") instanceof UserEntity) {
//            System.out.println("logged in");
//        } else {
//            System.out.println("logged out");
//        }
//        return "user/info";
//        if (session.getAttribute("signedUser") == null) { // 로그인이 안된 상태
//            return "redirect:/user/info";
//        } else { // 로그인이 된 상태
//            return "user/login";
//        }
        return user == null ? "redirect:/user/login" : "user/info";
    }

    @RequestMapping(value = "/info", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody         //  ↓ 현재 로그인한 사용자
    public String patchInfo(@SessionAttribute(value = "signedUser") UserEntity user,
                            @RequestParam(value = "currentPassword") String currentPassword, // 현재 비밀번호(대조용, 필수)
                            @RequestParam(value = "newPassword") String newPassword, // 신규 빌밀번호 (선택)
                            @RequestParam(value = "nickname") String nickname, // 변경할 닉네임(면경하지 않더라도 현재값 그대로, 필수)
                            @RequestParam(value = "birth", required = false) String birth) {
        ModifyResult result = this.userService.modify(user, currentPassword, newPassword, nickname, birth);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        return response.toString();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getLogin(@SessionAttribute(value =  "signedUser", required = false) UserEntity user) {
        if (user == null) { // 로그인이 안되어 있으면
            return "user/login";
        } else {
            //로그인이 된 상태면
            return "redirect:/user/info"; // templates/user/info.html로 변환하는게 아니고, 주소 자체를 /user/info로 옮기라는 의미
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postLogin(HttpSession session, UserEntity user) {
        LoginResult result = this.userService.login(user);
        if (result == LoginResult.SUCCESS) {
            session.setAttribute("signedUser", user);
        }
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        return response.toString();
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.TEXT_HTML_VALUE)
    public String getLogout(HttpSession session) {
        /* 로그아웃 로직
        * 로그아웃 후 /user/login 페이지로 리다이렉트 */
        session.setAttribute("signedUser", null);
        return "redirect:/user/login";
    }

    @RequestMapping(value = "/recover", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getRecover(@SessionAttribute(value = "signedUser", required = false) UserEntity user) {
        if (user != null) {
            return "redirect:/user/my";
        }
        return "user/recover";
    }

    /*
    * <a href="http://localhost:8080/user/register"> 회원가입 뷰 맵핑 </a>
    * @return 뷰 템플릿 /templates/user/register.html
    */

    @RequestMapping(value = "/register", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getRegister() {
        return "user/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRegister(UserEntity user,
                               @RequestParam(value = "isMarketingChecked") boolean isMarketingChecked) {
        RegisterResult result = this.userService.register(user, isMarketingChecked);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        return response.toString();
    }
}