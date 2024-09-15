package com.yourssu.blogyourssu.dto.response;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String email;
    private String username;

    public UserResponse(UserEntity userEntity) {
        email = userEntity.getEmail();
        username = userEntity.getUsername();
    }
}
