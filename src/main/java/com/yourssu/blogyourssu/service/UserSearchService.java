package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.reposiotry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSearchService {
    private final UserRepository userRepository;

    public UserEntity findById(Long userId){
        return userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException(userId + "에 해당하는 유저가 없습니다."));
    }
}
