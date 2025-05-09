package com.yjb.studyuserbbs.services;

import com.yjb.studyuserbbs.entities.ArticleEntity;
import com.yjb.studyuserbbs.entities.BoardEntity;
import com.yjb.studyuserbbs.entities.UserEntity;
import com.yjb.studyuserbbs.mappers.ArticleMapper;
import com.yjb.studyuserbbs.mappers.BoardMapper;
import com.yjb.studyuserbbs.results.article.DeleteResult;
import com.yjb.studyuserbbs.results.article.ModifyResult;
import com.yjb.studyuserbbs.results.article.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final BoardMapper boardMapper;

    @Autowired
    public ArticleService(ArticleMapper articleMapper, BoardMapper boardMapper) {
        this.articleMapper = articleMapper;
        this.boardMapper = boardMapper;
    }

    public DeleteResult deleteByIndex(UserEntity signedUser, int index) {

//    1. 매개변수 index가 1 미만이면 FAILURE_ABSENT
        if (index < 1) {
            System.out.println("FAILURE_ABSENT - 1");
            return DeleteResult.FAILURE_ABSENT;
        }
//    2. 매개변수 signedUser가 null이거나 signedUser의 isDeleted 혹은 isSuspended가 true라면 FAILURE_SESSION_EXPIRED
        if (signedUser == null ||
                signedUser.isDeleted() ||
                signedUser.isSuspended()) {
            System.out.println("FAILURE_SESSION_EXPIRED - 1");
            return DeleteResult.FAILURE_SESSION_EXPIRED;
        }
//    3. 매개변수 index로 articleMapper.selectByIndex한 결과가 null이라면 FAILURE_ABSENT
        ArticleEntity dbArticle = articleMapper.selectByIndex(index);
        if (dbArticle == null) {
            System.out.println("FAILURE_ABSENT - 2");
            return DeleteResult.FAILURE_ABSENT;
        }
//    4. <3>의 isDeleted가 true라면 FAILURE_ABSENT
        if (dbArticle.isDeleted()) {
            System.out.println("FAILURE_ABSENT - 3");
            return DeleteResult.FAILURE_ABSENT;
        }
//    5. 매개변수 signedUser의 isAdmin이 false이고 <3>의 userEmail의 email이 다르면 FAILURE_SESSION_EXPIRED
        if (!signedUser.isAdmin() && !dbArticle.getUserEmail().equals(signedUser.getEmail())) {
            System.out.println("FAILURE_SESSION_EXPIRED - 2");
            return DeleteResult.FAILURE_SESSION_EXPIRED;
        }
//    6. <3>의 isDeleted를 true로 지정하고 articleMapper.update 호출
        dbArticle.setDeleted(true);
//    7. <6>의 결과가 0보다 크면 SUCCESS 아니라면 FAILURE
        return this.articleMapper.update(dbArticle) > 0 ? DeleteResult.SUCCESS : DeleteResult.FAILURE;
    }

    public ArticleEntity get(int index) {
        if (index < 1) {
            return null;
        }
        ArticleEntity article = this.articleMapper.selectByIndex(index);
        if (article == null || article.isDeleted()) {
            return null;
        }
        return article;
    }

    public boolean incrementView(ArticleEntity article) {
        /*
         * 매개변수 article에 해당하는 게시글의 조회수를 1 올릴 수 있는 로직 작성
         * 1. 매개변수 article이 null이거나 이가 가지는 index가 1미만이라면 false 반환
         * 2. 매개변수 article의 view를 현재 가지고 있는 값 보다 1 더 크게 지정.
         * 3. ArticleMapper의 update 메서드를 활용하여 DB상에서 수정
         * 4. <3>의 결과가 0보다 크다면 true, 아니라면 false를 반환
         * */
        if (article == null || article.getIndex() < 1) {
            return false;
        }
        article.setView(article.getView() + 1);
        return this.articleMapper.update(article) > 0;
    }

    public ModifyResult modify(UserEntity signedUser, ArticleEntity article) {
        // 1. article이 null이거나 index가 1미만이면 FAILURE_ABSENT
        if (article == null || article.getIndex() < 1) {
            System.out.println(1);
            return ModifyResult.FAILURE_ABSENT;
        }
        // 2. article의 title이 null이거나 isEmpty가 true거나 length가 100자 초과, content가 null이거나 isEmpty가 true, length가 10000자 초과면 FAILURE
        if (article.getTitle() == null ||
                article.getTitle().isEmpty() ||
                article.getTitle().length() > 100 ||
                article.getContent() == null ||
                article.getContent().isEmpty() ||
                article.getContent().length() > 10000) {
            System.out.println(2);
            return ModifyResult.FAILURE;
        }
        // 3. signedUser가 null이거나 isDeleted가 true, isSuspended가 true면 FAILURE_SESSION_EXPIRED
        if (signedUser == null ||
                signedUser.isDeleted() ||
                signedUser.isSuspended()) {
            System.out.println(3);
            return ModifyResult.FAILURE_SESSION_EXPIRED;
        }
        // 4. articleMapper.selectByIndex에 article의 index를 전달하여 ArticleEntity 객체 반환
        ArticleEntity dbArticle = articleMapper.selectByIndex(article.getIndex());
        // 5. <4>가 null이거나 isDeleted가 true면 FAILURE_SESSION_EXPIRED
        if (dbArticle == null || dbArticle.isDeleted()) {
            System.out.println(5);
            return ModifyResult.FAILURE_ABSENT;
        }
        // 6. <4>의 userEmail과 signedUser의 email이 다르고, signedUser의 isAdmin이 false라면 FAILURE_SESSION_EXPIRED
        if (!dbArticle.getUserEmail().equals(signedUser.getEmail()) && !signedUser.isAdmin()) {
            System.out.println(6);
            return ModifyResult.FAILURE_SESSION_EXPIRED;
        }
        // 7. <4>의 title을 article의 title로, <4>의 content를 article의 content로, <4>의 modifiedAt을 현재 일시로 지정 후 articleMapper.update의 인자로 전달하여 호출
        dbArticle.setTitle(article.getTitle());
        dbArticle.setContent(article.getContent());
        dbArticle.setModifiedAt(LocalDateTime.now());
        // 8. <7>의 결과가 0을 초과한다면 SUCCESS, 아니라면 FAILURE
        return this.articleMapper.update(dbArticle) > 0 ? ModifyResult.SUCCESS : ModifyResult.FAILURE;
    }

    public WriteResult write(UserEntity signedUser, ArticleEntity article) {
        if (signedUser == null ||
                signedUser.isDeleted() ||
                signedUser.isSuspended()) {
            /* 로그인 안 했거나 || 탈퇴 했거나 || 정지된 계정이면 */
            return WriteResult.FAILURE_SESSION_EXPIRED;
        }
        if (article == null ||
                article.getBoardId() == null ||
                article.getTitle() == null ||
                article.getContent() == null ||
                article.getTitle().isEmpty() ||
                article.getTitle().length() > 100 ||
                article.getContent().isEmpty() ||
                article.getContent().length() > 10_000)
            /* 일반 정규화 */ {
            return WriteResult.FAILURE;
        }

        BoardEntity dbBoard = this.boardMapper.selectById(article.getBoardId());
        if (dbBoard == null) {
            /* 그런 게시판이 없다면 */
            return WriteResult.FAILURE;
        }
        if (dbBoard.isAdminOnly() && signedUser.isAdmin()) {
            return WriteResult.FAILURE;
        }

        article.setUserEmail(signedUser.getEmail());
        article.setView(0);
        article.setCreatedAt(LocalDateTime.now());
        article.setModifiedAt(null);
        article.setDeleted(false);

        return this.articleMapper.insert(article) > 0 ? WriteResult.SUCCESS : WriteResult.FAILURE;
    }
}
