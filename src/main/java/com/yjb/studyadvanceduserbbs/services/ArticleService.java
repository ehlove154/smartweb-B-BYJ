package com.yjb.studyadvanceduserbbs.services;

import com.yjb.studyadvanceduserbbs.entities.ArticleEntity;
import com.yjb.studyadvanceduserbbs.entities.BoardEntity;
import com.yjb.studyadvanceduserbbs.entities.UserEntity;
import com.yjb.studyadvanceduserbbs.mappers.ArticleMapper;
import com.yjb.studyadvanceduserbbs.mappers.BoardMapper;
import com.yjb.studyadvanceduserbbs.results.CommonResult;
import com.yjb.studyadvanceduserbbs.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleService {

    public static boolean isTitleValid(String input) {
        return input != null && input.matches("^(.{1,100})$");
    }

    public static boolean isContentValid(String input) {
        return input != null && input.matches("^(.{1,100000})$");
    }

    private final BoardMapper boardMapper;
    private final ArticleMapper articleMapper;

    @Autowired
    public ArticleService(BoardMapper boardMapper, ArticleMapper articleMapper) {
        this.boardMapper = boardMapper;
        this.articleMapper = articleMapper;
    }

    public ArticleEntity getById(int id) {
        if (id < 1) {
            return null;
        }
        return this.articleMapper.selectById(id);
    }

    public Result write(UserEntity user, ArticleEntity article) {
        if (user == null ||
                user.isDeleted() ||
                user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        // article INSERT

        if (article == null ||
                !BoardService.isIdValid(article.getBoardId()) ||
                !ArticleService.isTitleValid(article.getTitle()) ||
                !ArticleService.isContentValid(article.getContent())) {
            return CommonResult.FAILURE;
        }

        BoardEntity dbBoard = this.boardMapper.selectById(article.getBoardId());
        if (dbBoard == null) {
            return CommonResult.FAILURE;
        }
        if (dbBoard.isAdminOnly() &&
                user.isAdmin()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }
        article.setUserEmail(user.getEmail());
        article.setView(0);
        article.setCreatedAt(LocalDateTime.now());
        article.setModifiedAt(null);
        article.setDeleted(false);

        return this.articleMapper.insert(article) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result delete(UserEntity user, int id) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        ArticleEntity dbArticle = this.articleMapper.selectById(id);
        if (dbArticle == null || dbArticle.isDeleted()) {
            return CommonResult.FAILURE_ABSENT;
        }

        if (!dbArticle.getUserEmail().equals(user.getEmail()) && !user.isAdmin()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        dbArticle.setDeleted(true);

        return this.articleMapper.update(dbArticle) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result modify(UserEntity user, ArticleEntity article) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        if (article == null ||
                article.getId() < 1 ||
                !ArticleService.isTitleValid(article.getTitle()) ||
                !ArticleService.isContentValid(article.getContent())) {
            return CommonResult.FAILURE;
        }

        ArticleEntity dbArticle = this.articleMapper.selectById(article.getId());
        if (dbArticle == null || dbArticle.isDeleted()) {
            return CommonResult.FAILURE_ABSENT;
        }

        if (!dbArticle.getUserEmail().equals(user.getEmail()) && !user.isAdmin()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        dbArticle.setTitle(article.getTitle());
        dbArticle.setContent(article.getContent());
        dbArticle.setModifiedAt(LocalDateTime.now());

        return this.articleMapper.update(dbArticle) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public ArticleEntity getPrevious (int id) {
        if (id < 1) {
            return null;
        }
        return this.articleMapper.selectPrevious(id);
    }

    public ArticleEntity getNext (int id) {
        if (id < 1) {
            return null;
        }
        return this.articleMapper.selectNext(id);
    }

    public Result incrementView(ArticleEntity article) {
        if (article == null || article.getId() < 1) {
            return CommonResult.FAILURE;
        }

        article.setView(article.getView() + 1);
        return this.articleMapper.update(article) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }
}
