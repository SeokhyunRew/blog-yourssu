package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-19.
 */

import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_LOGIN_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
import com.yourssu.blogyourssu.reposiotry.UserRepository;
import com.yourssu.blogyourssu.testutil.SetupDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@Slf4j
public class ArticleSearchServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleSearchService articleSearchService;

    @Autowired
    ArticleService articleService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
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
    @DisplayName("게시글 조회 테스트")
    void SearchArticleTest() {
        // given
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        ArticleEntity saveArticle= articleRepository.save(SetupDataUtils.makeTestArticle(user));

        // when
        ArticleEntity findArticle = articleSearchService.findById(saveArticle.getId());

        // then
        assertThat(saveArticle.getId()).isEqualTo(findArticle.getId());
        assertThat(saveArticle.getTitle()).isEqualTo(findArticle.getTitle());
        assertThat(saveArticle.getContent()).isEqualTo(findArticle.getContent());
        assertThat(saveArticle.getUserEntity()).isEqualTo(findArticle.getUserEntity());
        assertThat(saveArticle.getCreate_at()).isEqualTo(findArticle.getCreate_at());
        assertThat(saveArticle.getUpdated_at()).isEqualTo(findArticle.getUpdated_at());
    }
}
