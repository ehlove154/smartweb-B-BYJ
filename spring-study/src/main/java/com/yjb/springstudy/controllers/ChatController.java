package com.yjb.springstudy.controllers;

import org.json.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/chat")
public class ChatController {
    // 대화 내역을 저장할 공간
    // 이는 정적이기 때문에 모든 스레드에서 공유하고, 서버를 재시작하지 않는 이상 유지된다.
    private static final List<String> messages = new ArrayList<>();

    @RequestMapping(value = "/home", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getHome() {
        return "chat/home";
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getMessage() {
        JSONArray response = new JSONArray(); // 새로운 JSONArray 객체 생성 ( [] )
        for (String message : messages) { // 멤버 변수 messages 가 가지고 있는 인자 문자열 message 에 대해 반복
            response.put(message); // JSONArray 객체에 추가
        }
        return response.toString(); // JSONArray 객체를 문자열로 변환하여 반환
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postMessage(@RequestParam(value = "message") String message) {
        messages.add(message); // 전달받은 message 문자열을 멤버 변수 messages에 추가
        return "뭐"; // 전달 받은 message 문자열에 대한 내용 검사 등을 수행하여 추가 결과를 반환하는게 맞겠으나 여기서는 생략.
    }
}