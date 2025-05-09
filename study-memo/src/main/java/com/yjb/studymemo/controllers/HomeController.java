package com.yjb.studymemo.controllers;

import com.yjb.studymemo.entities.MemoEntity;
import com.yjb.studymemo.results.memo.AddResult;
import com.yjb.studymemo.results.memo.DeleteByIndexResult;
import com.yjb.studymemo.services.MemoService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
public class HomeController {
    private final MemoService memoService;

    @Autowired
    public HomeController(MemoService memoService) {
        this.memoService = memoService;
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteIndex(@RequestParam(value = "index") int index) {
        DeleteByIndexResult result = memoService.deleteByIndex(index);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        return response.toString();
    }

    // http://localhost:8080/
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex() {
        // templates/
        return "home/index";
        //                      .html
        // > templates/home/index.html
    }
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postIndex(@RequestParam(value = "writer") String writer,
                            @RequestParam(value = "content") String content) {
        AddResult result = this.memoService.add(writer, content);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        return response.toString();
    }

    @RequestMapping(value = "/memos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MemoEntity[] getMemos() {
        return this.memoService.getAll();
    }
}
