package com.yourssu.blogyourssu.controller;/*
 * created by seokhyun on 2024-09-19.
 */

import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_COMMENT_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_COMMENT_UPDATE_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_LOGIN_EMAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.CommentEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.CommentRequest;
import com.yourssu.blogyourssu.dto.response.CommentResponse;
import com.yourssu.blogyourssu.jwt.CustomUserDetails;
import com.yourssu.blogyourssu.jwt.CustomUserDetailsService;
import com.yourssu.blogyourssu.jwt.JWTUtil;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
import com.yourssu.blogyourssu.reposiotry.CommentRepository;
import com.yourssu.blogyourssu.reposiotry.UserRepository;
import com.yourssu.blogyourssu.service.CommentSearchService;
import com.yourssu.blogyourssu.service.CommentService;
import com.yourssu.blogyourssu.testutil.SetupDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class CommentControllerTest {

    @Autowired
    CommentRepository commentRepository;

    @MockBean
    CommentService commentService;

    @Autowired
    CommentSearchService commentSearchService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

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
        articleRepository.deleteAll();;
    }

    @Test
    @DisplayName("댓글 생성 컨트롤러 테스트")
    void createCommentTest() throws Exception {
        // given
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        Long userId = user.getId();

        String access = jwtUtil.createJwt(TEST_LOGIN_EMAIL, null, userId, 60 * 10 * 1000L);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(TEST_LOGIN_EMAIL);

        ArticleEntity article = articleRepository.save(SetupDataUtils.makeTestArticle(user));
        Long articleId = article.getId();

        CommentRequest request = new CommentRequest(TEST_COMMENT_CONTENT);
        CommentResponse mockResponse = new CommentResponse(1l, TEST_COMMENT_CONTENT);

        // UserService.createAccount() 호출을 모킹하여 원하는 응답 반환
        when(commentService.createComment(any(CommentRequest.class), eq(userId), eq(articleId) ))
                .thenReturn(mockResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/article/{articleId}/comment", articleId)
                .header("Authorization", "Bearer " + access)
                .contentType(MediaType.APPLICATION_JSON)// Authorization 헤더로 변경
                .content(new ObjectMapper().writeValueAsString(request))
                .with(user(customUserDetails)));  // JSON request body

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(TEST_COMMENT_CONTENT))
                .andDo(print());

        verify(commentService).createComment(any(CommentRequest.class), eq(userId), eq(articleId) );
    }

    @Test
    @DisplayName("댓글 수정 컨트롤러 테스트")
    void updateCommentTest() throws Exception {
        // given
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        Long userId = user.getId();

        String access = jwtUtil.createJwt(TEST_LOGIN_EMAIL, null, userId, 60 * 10 * 1000L);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(TEST_LOGIN_EMAIL);

        ArticleEntity article = articleRepository.save(SetupDataUtils.makeTestArticle(user));

        CommentEntity commentEntity = commentRepository.save(SetupDataUtils.makeTestComment(user, article));
        Long commentId = commentEntity.getId();

        CommentRequest request = new CommentRequest(TEST_COMMENT_UPDATE_CONTENT);
        CommentResponse mockResponse = new CommentResponse(1l, TEST_COMMENT_UPDATE_CONTENT);

        // UserService.createAccount() 호출을 모킹하여 원하는 응답 반환
        when(commentService.updateComment(eq(commentId), eq(userId), any(CommentRequest.class)))
                .thenReturn(mockResponse);

        // when
        ResultActions result = mockMvc.perform(put("/api/comment/{commentId}", commentId)
                .header("Authorization", "Bearer " + access)
                .contentType(MediaType.APPLICATION_JSON)// Authorization 헤더로 변경
                .content(new ObjectMapper().writeValueAsString(request))
                .with(user(customUserDetails)));  // JSON request body

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(TEST_COMMENT_UPDATE_CONTENT))
                .andDo(print());

        verify(commentService).updateComment(eq(commentId), eq(userId), any(CommentRequest.class));
    }

    @Test
    @DisplayName("댓글 삭제 컨트롤러 테스트")
    void deleteCommentTest() throws Exception {
        // given
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        Long userId = user.getId();

        String access = jwtUtil.createJwt(TEST_LOGIN_EMAIL, null, userId, 60 * 10 * 1000L);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(TEST_LOGIN_EMAIL);

        ArticleEntity article = articleRepository.save(SetupDataUtils.makeTestArticle(user));

        CommentEntity commentEntity = commentRepository.save(SetupDataUtils.makeTestComment(user, article));
        Long commentId = commentEntity.getId();

        // UserService.createAccount() 호출을 모킹하여 원하는 응답 반환
        doNothing().when(commentService).deleteById(eq(commentId), eq(userId));

        // when
        ResultActions result = mockMvc.perform(delete("/api/comment/{commentId}", commentId)
                .header("Authorization", "Bearer " + access)
                .contentType(MediaType.APPLICATION_JSON)// Authorization 헤더로 변경
                .with(user(customUserDetails)));  // JSON request body

        // then
        result.andExpect(status().isOk())
                .andDo(print());

        verify(commentService).deleteById(eq(commentId), eq(userId));
    }


}
