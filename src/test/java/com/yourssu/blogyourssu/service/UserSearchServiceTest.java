package com.yourssu.blogyourssu.service;/*
* * created by seokhyun on 2024-09-19.
 */

import static com.yourssu.blogyourssu.testutil.SetupDataUtils.TEST_LOGIN_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

import com.yourssu.blogyourssu.domain.UserEntity;
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
public class UserSearchServiceTest {

    @Autowired
    UserSearchService userSearchService;

    @Autowired
    UserRepository userRepository;

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
    @DisplayName("유저 조회 테스트")
    void SearchUserTest() {
        // given
        UserEntity user = userRepository.findByEmail(TEST_LOGIN_EMAIL);

        // when
        UserEntity findUser = userSearchService.findById(user.getId());

        // then
        assertThat(user.getId()).isEqualTo(findUser.getId());
        assertThat(user.getEmail()).isEqualTo(findUser.getEmail());
        assertThat(user.getPassword()).isEqualTo(findUser.getPassword());
        assertThat(user.getUsername()).isEqualTo(findUser.getUsername());
        assertThat(user.getRole()).isEqualTo(findUser.getRole());
        assertThat(user.getCreate_at()).isEqualTo(findUser.getCreate_at());
        assertThat(user.getUpdated_at()).isEqualTo(findUser.getUpdated_at());
    }
}
