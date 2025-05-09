package com.yjb.studyuserbbs.controllers;

import com.yjb.studyuserbbs.entities.ArticleEntity;
import com.yjb.studyuserbbs.entities.BoardEntity;
import com.yjb.studyuserbbs.entities.UserEntity;
import com.yjb.studyuserbbs.results.article.DeleteResult;
import com.yjb.studyuserbbs.results.article.ModifyResult;
import com.yjb.studyuserbbs.results.article.WriteResult;
import com.yjb.studyuserbbs.services.ArticleService;
import com.yjb.studyuserbbs.services.BoardService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {
    private final ArticleService articleService;
    private final BoardService boardService;

    @Autowired
    public ArticleController(ArticleService articleService, BoardService boardService) {
        this.articleService = articleService;
        this.boardService = boardService;
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteIndex(@SessionAttribute(value = "signedUser", required = false)UserEntity signedUser,
                              @RequestParam(value = "index", required = false, defaultValue = "0")int index) {
        DeleteResult result = this.articleService.deleteByIndex(signedUser, index);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        return response.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex(@RequestParam(value = "index", required = false, defaultValue = "0") int index, Model model) {
        ArticleEntity article = this.articleService.get(index);
        /*
        * 1. 위 article이 실존한다면 조회수 올리기(반환되는 true/false는 활용하지 않음)
        * 2. 위 article이 실존한다면 이가 가지는 boardId를 통해 "article/index" 페이지에 게시판 제목 출력하기
        * */
        if (article != null) {
            this.articleService.incrementView(article);
            BoardEntity board = this.boardService.getById(article.getBoardId());
            model.addAttribute("board", board);
        }
        model.addAttribute("article", article);

        return "article/index";
    }

    @RequestMapping(value = "/modify", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getModify(@RequestParam(value = "index", required = false, defaultValue = "0") int index,
                            Model model) {
        ArticleEntity article = this.articleService.get(index);
        if (article != null) {
            BoardEntity board = this.boardService.getById(article.getBoardId());
            model.addAttribute("board", board);
        }
        model.addAttribute("article", article);
        return "article/modify";
    }

    @RequestMapping(value = "/modify", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModify(@SessionAttribute(value = "signedUser", required = false)UserEntity signedUser,
                              ArticleEntity article) {
        ModifyResult result = this.articleService.modify(signedUser, article);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        return response.toString();
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getWrite(@SessionAttribute(value = "signedUser", required = false) UserEntity user, @RequestParam(value = "boardId", required = false, defaultValue = "") String boardId, Model model) {
        BoardEntity board = this.boardService.getById(boardId);
        System.out.println(board == null);
        model.addAttribute("board", board);
        return "article/write";
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, ArticleEntity article) {
        WriteResult result = this.articleService.write(signedUser, article);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        if (result == WriteResult.SUCCESS) {
            response.put("index", article.getIndex());
        }
        return response.toString();
    }
}
