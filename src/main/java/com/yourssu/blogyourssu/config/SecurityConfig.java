package com.yourssu.blogyourssu.config;/*
 * created by seokhyun on 2024-09-16.
 */

//-------------------------------------Spring Security 6.x 버전부터 변경 사항  >> 기존에 arguments(인수)로 입력받던 방식을 FunctionalInterface로 입력받도록 변경--------------
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    //swaggerConfig 사용하게
    @Bean
    public WebSecurityCustomizer configure() {      // 스프링 시큐리티 기능 비활성화
        return web -> web.ignoring()
                .requestMatchers( "/");
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth ->              // 인증, 인가 설정
                        auth.anyRequest().permitAll())
                .csrf(auth -> auth.disable());                  // csrf 비활성화
        return httpSecurity.build();
    }

    //비밀번호 인코딩 빈 주입
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
