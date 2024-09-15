package com.yourssu.blogyourssu.dto.util;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.response.UserResponse;

public class UserDtoUtil {
    public static UserResponse userToUserResponse(UserEntity userEntity){
        return  UserResponse.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .build();
    }
}
