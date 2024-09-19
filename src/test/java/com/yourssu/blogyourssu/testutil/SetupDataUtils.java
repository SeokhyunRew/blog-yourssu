package com.yourssu.blogyourssu.testutil;/*
 * created by seokhyun on 2024-09-19.
 */

import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.CommentEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SetupDataUtils {

    // 테스트용 유저 데이터 Input
    public static final String TEST_LOGIN_EMAIL = "test@test.com";
    public static final String TEST_PASSWORD = "testPassword";
    public static final String TEST_USERNAME = "test";

    // 테스트용 게시글 데이터 Input
    public static final String TEST_TITLE = "게시글 제목";
    public static final String TEST_ARTICLE_CONTENT = "게시글 내용";

    // 테스트용 댓글 데이터 Input
    public static final String TEST_COMMENT_CONTENT = "댓글 내용";

    private SetupDataUtils() {
    }

    public static UserEntity makeTestUser(BCryptPasswordEncoder passwordEncoder) {
        return new UserEntity(
                null,
                TEST_LOGIN_EMAIL,
                passwordEncoder.encode(TEST_PASSWORD),
                TEST_USERNAME,
                null);
    }

    public static UserEntity makeCustomTestUser(Long id, String email, String password, String username, BCryptPasswordEncoder passwordEncoder) {
        return new UserEntity(
                id,
                email,
                passwordEncoder.encode(password),
                username,
                null);
    }

    public static ArticleEntity makeTestArticle() {
        return new ArticleEntity(
                null,
                TEST_TITLE,
                TEST_ARTICLE_CONTENT,
                null);
    }

    public static CommentEntity makeTestComment() {
        return new CommentEntity(
                null,
                TEST_ARTICLE_CONTENT,
                null,
                null);
    }

}
