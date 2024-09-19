package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-19.
 */

import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_LOGIN_EMAIL;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_PASSWORD;
import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.yourssu.blogyourssu.common.exception.customexception.NotFoundException;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.UserRequest;
import com.yourssu.blogyourssu.dto.response.UserResponse;
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
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserSearchService userSearchService;

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
    @DisplayName("회원가입 성공 테스트")
    void createAccountTest() {
        // given
        UserRequest request = new UserRequest(TEST_LOGIN_EMAIL + 1, TEST_PASSWORD, TEST_USERNAME);

        // when
        UserResponse response = userService.createAccount(request);
        UserEntity findUser = userRepository.findByEmail(response.getEmail());

        // then
        assertEquals(TEST_LOGIN_EMAIL + 1, response.getEmail());
        assertEquals(findUser.getEmail(), response.getEmail());
    }

    @Test
    @DisplayName("회원 삭제 성공 테스트")
    void deleteUserTest() {
        // given
        UserEntity findUser = userRepository.findByEmail(TEST_LOGIN_EMAIL);

        // when
        userService.delete(findUser.getId());

        // then
        assertThrows(NotFoundException.class, () -> userSearchService.findById(findUser.getId()));
    }
}