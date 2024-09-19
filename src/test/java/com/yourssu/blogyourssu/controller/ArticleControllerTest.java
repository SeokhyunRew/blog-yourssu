package com.yourssu.blogyourssu.controller;/*
 * created by seokhyun on 2024-09-19.
 */

import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_ARTICLE_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_ARTICLE_UPDATE_CONTENT;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_LOGIN_EMAIL;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_UPDATE_TITLE;
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
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.ArticleRequest;
import com.yourssu.blogyourssu.dto.response.ArticleResponse;
import com.yourssu.blogyourssu.jwt.CustomUserDetails;
import com.yourssu.blogyourssu.jwt.CustomUserDetailsService;
import com.yourssu.blogyourssu.jwt.JWTUtil;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
import com.yourssu.blogyourssu.reposiotry.UserRepository;
import com.yourssu.blogyourssu.service.ArticleSearchService;
import com.yourssu.blogyourssu.service.ArticleService;
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
public class ArticleControllerTest {

    @MockBean
    ArticleService articleService;

    @Autowired
    ArticleSearchService articleSearchService;

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
        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
    }

    @AfterEach
    void afterEach() {
        log.info("after each");

        //테스트 하나 끝날때 마다 삭제
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 생성 컨트롤러 테스트")
    void createArticleTest() throws Exception {
        // given
        ArticleRequest request = new ArticleRequest(TEST_UPDATE_TITLE, TEST_ARTICLE_CONTENT);
        ArticleResponse mockResponse = new ArticleResponse(1l, TEST_UPDATE_TITLE, TEST_ARTICLE_CONTENT);

        Long userId = userRepository.findByEmail(TEST_LOGIN_EMAIL).getId();
        String access = jwtUtil.createJwt(TEST_LOGIN_EMAIL, null, userId, 60 * 10 * 1000L);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(TEST_LOGIN_EMAIL);

        // UserService.createAccount() 호출을 모킹하여 원하는 응답 반환
        when(articleService.createArticle(any(ArticleRequest.class), eq(userId) ))
                .thenReturn(mockResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/article")
                        .header("Authorization", "Bearer " + access)
                        .contentType(MediaType.APPLICATION_JSON)// Authorization 헤더로 변경
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(user(customUserDetails)));  // JSON request body

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(TEST_UPDATE_TITLE))
                .andExpect(jsonPath("$.content").value(TEST_ARTICLE_CONTENT))
                .andDo(print());

        verify(articleService).createArticle(any(ArticleRequest.class), eq(userId));
    }

    @Test
    @DisplayName("게시글 수정 컨트롤러 테스트")
    void updateArticleTest() throws Exception {
        // given
        Long articleId = 1L;
        ArticleRequest request = new ArticleRequest(TEST_UPDATE_TITLE, TEST_ARTICLE_UPDATE_CONTENT);
        ArticleResponse mockResponse = new ArticleResponse(articleId, TEST_UPDATE_TITLE, TEST_ARTICLE_UPDATE_CONTENT);

        Long userId = userRepository.findByEmail(TEST_LOGIN_EMAIL).getId();
        String access = jwtUtil.createJwt(TEST_LOGIN_EMAIL, null, userId, 60 * 10 * 1000L);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(TEST_LOGIN_EMAIL);

        // articleService.updateArticle() 호출을 모킹하여 원하는 응답 반환
        when(articleService.updateArticle(eq(articleId), eq(userId), any(ArticleRequest.class)))
                .thenReturn(mockResponse);

        // when
        ResultActions result = mockMvc.perform(put("/api/article/{articleId}", articleId)
                .header("Authorization", "Bearer " + access)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .with(user(customUserDetails)));  // JSON request body

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(TEST_UPDATE_TITLE))
                .andExpect(jsonPath("$.content").value(TEST_ARTICLE_UPDATE_CONTENT))
                .andDo(print());

        verify(articleService).updateArticle(eq(articleId), eq(userId), any(ArticleRequest.class));
    }


    @Test
    @DisplayName("유저 삭제 컨트롤러 테스트")
    void deleteArticleTest() throws Exception {
        // Given
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);
        Long userId = user.getId();

        String access = jwtUtil.createJwt(TEST_LOGIN_EMAIL, null, userId, 60 * 10 * 1000L);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(TEST_LOGIN_EMAIL);

        ArticleEntity saveArticle= articleRepository.save(SetupDataUtils.makeTestArticle(user));
        Long articleId = saveArticle.getId();

        // articleService.deleteById 메서드 호출을 모킹
        doNothing().when(articleService).deleteById(saveArticle.getId(), userId);

        // When
        mockMvc.perform(delete("/api/article/{articleId}", articleId)
                        .header("Authorization", "Bearer " + access)  // Authorization 헤더로 변경
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andDo(print());

        // Then
        verify(articleService).deleteById(articleId, userId);
    }

}
