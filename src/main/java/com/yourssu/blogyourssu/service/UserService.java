package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.CommentEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.UserRequest;
import com.yourssu.blogyourssu.dto.response.UserResponse;
import com.yourssu.blogyourssu.dto.util.UserDtoUtil;
import com.yourssu.blogyourssu.reposiotry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserSearchService userSearchService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createAccount(UserRequest request){
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
        UserEntity saveUser = userRepository.save(request.toEntity());

        return UserDtoUtil.userToUserResponse(saveUser);
    }

    public void delete(Long userId){
        UserEntity findUser = userSearchService.findById(userId);

        if (!findUser.getId().equals(userId)) {
            throw new IllegalArgumentException("본인만 회원탈퇴 가능합니다.");
        }

        userRepository.deleteById(userId);
    }
}
