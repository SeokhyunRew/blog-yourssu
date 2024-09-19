package com.yourssu.blogyourssu.controller;

import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_LOGIN_EMAIL;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_PASSWORD;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourssu.blogyourssu.dto.request.UserRequest;
import com.yourssu.blogyourssu.dto.response.UserResponse;
import com.yourssu.blogyourssu.jwt.CustomUserDetails;
import com.yourssu.blogyourssu.jwt.CustomUserDetailsService;
import com.yourssu.blogyourssu.jwt.JWTUtil;
import com.yourssu.blogyourssu.reposiotry.UserRepository;
import com.yourssu.blogyourssu.service.UserSearchService;
import com.yourssu.blogyourssu.service.UserService;
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
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    UserSearchService userSearchService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

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
    @DisplayName("회원 생성 컨트롤러 테스트")
    void createAccountTest() throws Exception {
        // given
        UserRequest request = new UserRequest(TEST_LOGIN_EMAIL + 1, TEST_PASSWORD, TEST_USERNAME);
        UserResponse mockResponse = new UserResponse(TEST_LOGIN_EMAIL + 1, TEST_USERNAME);

        // UserService.createAccount() 호출을 모킹하여 원하는 응답 반환
        when(userService.createAccount(any(UserRequest.class)))
                .thenReturn(mockResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/all/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)));  // JSON request body

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(TEST_LOGIN_EMAIL + 1))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andDo(print());

        verify(userService).createAccount(any(UserRequest.class));
    }

    @Test
    @DisplayName("유저 삭제 컨트롤러 테스트")
    void deleteUserTest() throws Exception {
        // Given
        Long userId = userRepository.findByEmail(TEST_LOGIN_EMAIL).getId();
        String access = jwtUtil.createJwt(TEST_LOGIN_EMAIL, null, userId, 60 * 10 * 1000L);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(TEST_LOGIN_EMAIL);

        // userService.delete 메서드 호출을 모킹
        doNothing().when(userService).delete(userId);

        // When
        mockMvc.perform(delete("/api/users")
                        .header("Authorization", "Bearer " + access)  // Authorization 헤더로 변경
                        .with(user(customUserDetails)))
                .andExpect(status().isNoContent())
                .andDo(print());

        // Then
        verify(userService).delete(userId);
    }

}
