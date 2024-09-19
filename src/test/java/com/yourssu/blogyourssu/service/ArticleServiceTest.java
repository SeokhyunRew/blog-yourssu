package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-19.
 */

import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_ARTICLE_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_ARTICLE_UPDATE_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_LOGIN_EMAIL;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_TITLE;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_UPDATE_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.yourssu.blogyourssu.common.exception.customexception.NotFoundException;
import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.ArticleRequest;
import com.yourssu.blogyourssu.dto.response.ArticleResponse;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Transactional
@SpringBootTest
@Slf4j
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleSearchService articleSearchService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

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
        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
    }

    @AfterEach
    void afterEach() {
        log.info("after each");

        //테스트 하나 끝날때 마다 삭제
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    void createArticleTest() {
        // given
        ArticleRequest request = new ArticleRequest(TEST_TITLE, TEST_ARTICLE_CONTENT);
        Long userId = userRepository.findByEmail(TEST_LOGIN_EMAIL).getId();

        // when
        ArticleResponse response = articleService.createArticle(request, userId);
        Optional<ArticleEntity> findArticle = articleRepository.findById(response.getId());

        // then
        assertEquals(TEST_TITLE, response.getTitle());
        assertEquals(findArticle.get().getId(), response.getId());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updateArticleTest() {
        // GIVEN
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        ArticleEntity saveArticle= articleRepository.save(SetupDataUtils.makeTestArticle(user));

        Long articleId = saveArticle.getId();
        Long userId = saveArticle.getUserEntity().getId();

        // WHEN
        ArticleRequest updateRequest = new ArticleRequest(TEST_UPDATE_TITLE,TEST_ARTICLE_UPDATE_CONTENT);
        ArticleResponse response = articleService.updateArticle( articleId , userId, updateRequest);

        // THEN
        Assertions.assertEquals(TEST_UPDATE_TITLE, response.getTitle());
        Assertions.assertEquals(TEST_ARTICLE_UPDATE_CONTENT, response.getContent());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletedArticleTest() {
        // GIVEN
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        ArticleEntity saveArticle= articleRepository.save(SetupDataUtils.makeTestArticle(user));

        // WHEN
        articleService.deleteById(saveArticle.getId(), saveArticle.getUserEntity().getId());

        // THEN
        assertThrows(NotFoundException.class, () -> articleSearchService.findById(saveArticle.getId()));
    }
}
