package com.yourssu.blogyourssu.dto.request;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class UserRequest {
    String email;
    String password;
    String username;
    BCryptPasswordEncoder encoder;


    public UserEntity toEntity(){
        return UserEntity.builder()
                .email(email)
                .password(encoder.encode(password))
                .username(username)
                .build();
    }
}
