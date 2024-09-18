package com.yourssu.blogyourssu.jwt;/*
 * created by seokhyun on 2024-09-16.
*/

import com.yourssu.blogyourssu.common.exception.customexception.NotFoundException;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.reposiotry.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회
        UserEntity userData = Optional.ofNullable(userRepository.findByEmail(username))
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다.") {
                });

        if (userData != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
