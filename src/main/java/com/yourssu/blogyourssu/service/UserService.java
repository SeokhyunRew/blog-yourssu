package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.UserRequest;
import com.yourssu.blogyourssu.dto.response.UserResponse;
import com.yourssu.blogyourssu.dto.util.UserDtoUtil;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
import com.yourssu.blogyourssu.reposiotry.CommentRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public UserResponse createAccount(UserRequest request){
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
        UserEntity saveUser = userRepository.save(request.toEntity());

        return UserDtoUtil.userToUserResponse(saveUser);
    }

    public void delete(Long userId){

        commentRepository.deleteByUserId(userId);
        commentRepository.deleteByArticleIdUseUserId(userId);
        articleRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
}
