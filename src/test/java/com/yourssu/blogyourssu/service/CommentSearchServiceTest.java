package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-19.
 */

import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_LOGIN_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.CommentEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
import com.yourssu.blogyourssu.reposiotry.CommentRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Slf4j
public class CommentSearchServiceTest {

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
        ArticleEntity article= articleRepository.save(SetupDataUtils.makeTestArticle(user));
        CommentEntity saveComment = commentRepository.save(SetupDataUtils.makeTestComment(user, article));

        // when
        CommentEntity findComment = commentSearchService.findById(saveComment.getId());

        // then
        assertThat(saveComment.getId()).isEqualTo(findComment.getId());
        assertThat(saveComment.getContent()).isEqualTo(findComment.getContent());
        assertThat(saveComment.getUserEntity()).isEqualTo(findComment.getUserEntity());
        assertThat(saveComment.getArticleEntity()).isEqualTo(findComment.getArticleEntity());
        assertThat(saveComment.getCreate_at()).isEqualTo(findComment.getCreate_at());
        assertThat(saveComment.getUpdated_at()).isEqualTo(findComment.getUpdated_at());
    }
}
