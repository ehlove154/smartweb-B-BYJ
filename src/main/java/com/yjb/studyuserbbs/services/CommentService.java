package com.yjb.studyuserbbs.services;

import com.yjb.studyuserbbs.entities.ArticleEntity;
import com.yjb.studyuserbbs.entities.CommentEntity;
import com.yjb.studyuserbbs.entities.UserEntity;
import com.yjb.studyuserbbs.mappers.ArticleMapper;
import com.yjb.studyuserbbs.mappers.CommentMapper;
import com.yjb.studyuserbbs.results.comment.DeleteResult;
import com.yjb.studyuserbbs.results.comment.ModifyResult;
import com.yjb.studyuserbbs.results.comment.WriteResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;

    public CommentService(ArticleMapper articleMapper, CommentMapper commentMapper) {
        this.articleMapper = articleMapper;
        this.commentMapper = commentMapper;
    }

    public DeleteResult deleteByIndex(UserEntity signedUser, int index) {
        // signedUser : 로그인한 사람, index로 삭제
        if (index < 1) {
            return DeleteResult.FAILURE;
        }
        if (signedUser == null ||
                signedUser.isDeleted() ||
                signedUser.isSuspended()) {
            return DeleteResult.FAILURE_SESSION_EXPIRED;
        }
        CommentEntity dbComment = commentMapper.selectByIndex(index);
        if (dbComment == null || dbComment.isDeleted()) {
            return DeleteResult.FAILURE_ABSENT;
        }
        if (!signedUser.isAdmin() && !dbComment.getUserEmail().equals(signedUser.getEmail())) {
            return DeleteResult.FAILURE_SESSION_EXPIRED;
        }
        dbComment.setDeleted(true);
        return this.commentMapper.update(dbComment) > 0 ? DeleteResult.SUCCESS : DeleteResult.FAILURE;
    }


    public CommentEntity getByIndex(int index) {
        // index로 댓글 반환
        if (index < 1) {
            return null;
        }
        CommentEntity comment = commentMapper.selectByIndex(index);
        if (comment == null || comment.isDeleted()) {
            return null;
        }
        return comment;
    }

    public CommentEntity[] getByArticleIndex(int articleIndex) {
        // articleIndex(게시글 식별자)로 댓글 배열 반환
        if (articleIndex < 1) {
            return new CommentEntity[0];
        }
        return new CommentEntity[]{commentMapper.selectByArticleIndex(articleIndex)};
    }

    public ModifyResult modify(UserEntity signedUser, CommentEntity comment) {
        // signedUser: 로그인한 사람. 댓글 수정. comment는 요첨에 따라 index랑 content만 가지고 있을 예정
        if (comment == null || comment.getIndex() < 1) {
            return ModifyResult.FAILURE_ABSENT;
        }
        if (signedUser == null ||
                signedUser.isDeleted() ||
                signedUser.isSuspended()) {
            return ModifyResult.FAILURE_SESSION_EXPIRED;
        }
        CommentEntity dbComment = commentMapper.selectByIndex(comment.getIndex());
        if (dbComment == null || dbComment.isDeleted()) {
            return ModifyResult.FAILURE_ABSENT;
        }
        if (!dbComment.getUserEmail().equals(signedUser.getEmail()) && !signedUser.isAdmin()) {
            return ModifyResult.FAILURE_SESSION_EXPIRED;
        }
        dbComment.setContent(comment.getContent());
        return this.commentMapper.update(dbComment) > 0 ? ModifyResult.SUCCESS : ModifyResult.FAILURE;
    }

    public WriteResult write(UserEntity signedUser, CommentEntity comment) {
        // signedUser: 로그인한 사람. 댓글 쓰기. comment는 요청에 따라 articleIndex, content, commentIndex(경우에 따라 null)만 가지고 있음.
        if (comment == null ||
                comment.getContent() == null ||
                comment.getContent().isEmpty() ||
                comment.getContent().length() > 1000 ||
                (comment.getCommentIndex() != null &&
                        comment.getCommentIndex() < 1)) {
            return WriteResult.FAILURE;
        }
        if (comment.getArticleIndex() < 1) {
            return WriteResult.FAILURE_ARTICLE_ABSENT;
        }
        if (signedUser == null ||
                signedUser.isDeleted() ||
                signedUser.isSuspended()) {
            return WriteResult.FAILURE_SESSION_EXPIRED;
        }
        ArticleEntity dbArticle = articleMapper.selectByIndex(comment.getArticleIndex());
        if (dbArticle == null || dbArticle.isDeleted()) {
            return WriteResult.FAILURE_ARTICLE_ABSENT;
        }

        comment.setUserEmail(signedUser.getEmail());
        comment.setCreateAt(LocalDateTime.now());
        comment.setDeleted(false);
        return this.commentMapper.insert(comment) > 0 ? WriteResult.SUCCESS : WriteResult.FAILURE;
    }
}