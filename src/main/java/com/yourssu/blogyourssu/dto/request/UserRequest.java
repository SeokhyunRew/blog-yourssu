package com.yourssu.blogyourssu.dto.request;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    String email;
    String password;
    String username;


    public UserEntity toEntity(){
        return UserEntity.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();
    }
}
