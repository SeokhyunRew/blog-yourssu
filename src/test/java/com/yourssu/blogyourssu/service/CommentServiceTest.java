package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-19.
 */

import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_ARTICLE_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_ARTICLE_UPDATE_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_COMMENT_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_COMMENT_UPDATE_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_LOGIN_EMAIL;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_TITLE;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_UPDATE_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.yourssu.blogyourssu.common.exception.customexception.NotFoundException;
import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.CommentEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.ArticleRequest;
import com.yourssu.blogyourssu.dto.request.CommentRequest;
import com.yourssu.blogyourssu.dto.response.ArticleResponse;
import com.yourssu.blogyourssu.dto.response.CommentResponse;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
import com.yourssu.blogyourssu.reposiotry.CommentRepository;
import com.yourssu.blogyourssu.reposiotry.UserRepository;
import com.yourssu.blogyourssu.testutil.SetupDataUtils;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@Slf4j
public class CommentServiceTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentSearchService commentSearchService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void beforeEach() {
        log.info("before each");

        //예외 처리 위해서 validator 설정
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        //기본 유저 한명 생성
        UserEntity user = userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
    }

    @AfterEach
    void afterEach() {
        log.info("after each");

        //테스트 하나 끝날때 마다 삭제
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void createCommentTest() {
        // given
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        Long userId = user.getId();

        ArticleEntity article = articleRepository.save(SetupDataUtils.makeTestArticle(user));
        Long articleId = article.getId();

        CommentRequest request = new CommentRequest(TEST_COMMENT_CONTENT);

        // when
        CommentResponse response = commentService.createComment(request, userId, articleId);
        Optional<CommentEntity> findComment = commentRepository.findById(response.getId());

        // then
        assertEquals(TEST_COMMENT_CONTENT, response.getContent());
        assertEquals(findComment.get().getId(), response.getId());
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void updateCommentTest() {
        // GIVEN
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        ArticleEntity saveArticle = articleRepository.save(SetupDataUtils.makeTestArticle(user));
        CommentEntity saveComment = commentRepository.save(SetupDataUtils.makeTestComment(user, saveArticle));

        Long commentId = saveComment.getId();
        Long userId = saveArticle.getUserEntity().getId();

        // WHEN
        CommentRequest updateRequest = new CommentRequest(TEST_COMMENT_UPDATE_CONTENT);
        CommentResponse response = commentService.updateComment(commentId, userId, updateRequest);

        // THEN
        Assertions.assertEquals(TEST_COMMENT_UPDATE_CONTENT, response.getContent());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletedCommentTest() {
        // GIVEN
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        ArticleEntity saveArticle= articleRepository.save(SetupDataUtils.makeTestArticle(user));
        CommentEntity saveComment = commentRepository.save(SetupDataUtils.makeTestComment(user, saveArticle));

        // WHEN
        commentService.deleteById(saveComment.getId(), saveComment.getUserEntity().getId());

        // THEN
        assertThrows(NotFoundException.class, () -> commentSearchService.findById(saveComment.getId()));
    }
}
