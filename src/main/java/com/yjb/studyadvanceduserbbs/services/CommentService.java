package com.yjb.studyadvanceduserbbs.services;

import com.yjb.studyadvanceduserbbs.entities.ArticleEntity;
import com.yjb.studyadvanceduserbbs.entities.CommentEntity;
import com.yjb.studyadvanceduserbbs.entities.UserEntity;
import com.yjb.studyadvanceduserbbs.mappers.ArticleMapper;
import com.yjb.studyadvanceduserbbs.mappers.CommentMapper;
import com.yjb.studyadvanceduserbbs.results.CommonResult;
import com.yjb.studyadvanceduserbbs.results.Result;
import com.yjb.studyadvanceduserbbs.results.ResultTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    private final CommentMapper commentMapper;
    private final ArticleMapper articleMapper;

    @Autowired
    public CommentService(CommentMapper commentMapper, ArticleMapper articleMapper) {
        this.commentMapper = commentMapper;
        this.articleMapper = articleMapper;
    }

    public ResultTuple<CommentEntity[]> getByArticleId(int articleId) {
        if (articleId < 1) {
            return ResultTuple.<CommentEntity[]>builder().result(CommonResult.FAILURE).build();
        }
        ArticleEntity dbArticle = this.articleMapper.selectById(articleId);
        if (dbArticle == null || dbArticle.isDeleted()) {
            return ResultTuple.<CommentEntity[]>builder().result(CommonResult.FAILURE_ABSENT).build();
        }
        return ResultTuple.<CommentEntity[]>builder().result(CommonResult.SUCCESS).payload(this.commentMapper.selectByArticleId(articleId)).build();
    }

    public Result write(UserEntity user, CommentEntity comment) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }
        if (comment == null || comment.getArticleId() < 1 || comment.getContent() == null || comment.getContent().isEmpty()) {
            return CommonResult.FAILURE;
        }
        ArticleEntity dbArticle = this.articleMapper.selectById(comment.getArticleId());
        if (dbArticle == null || dbArticle.isDeleted()) {
            return CommonResult.FAILURE_ABSENT;
        }
        comment.setUserEmail(user.getEmail());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setDeleted(false);
        return this.commentMapper.insert(comment) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }
}
