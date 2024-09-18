package com.yourssu.blogyourssu.jwt;/*
 * created by seokhyun on 2024-09-16.
 */

import static com.yourssu.blogyourssu.jwt.ExceptionHandlerUtil.handleException;
import com.yourssu.blogyourssu.domain.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorization.split(" ")[1];

            if (jwtUtil.isExpired(token)) {
                handleException(response, "Token has expired", HttpStatus.UNAUTHORIZED, request);
                return;
            }

            String email = jwtUtil.getEmail(token);
            String role = jwtUtil.getRole(token);
            Long userId = jwtUtil.getUserId(token);

            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setPassword("temppassword");
            userEntity.setId(userId);
            userEntity.setRole(role);

            CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (ExpiredJwtException ex) {
            handleException(response, "토큰 유효기간이 만료됐습니다. 다시 로그인해서 재발급해주세요!", HttpStatus.UNAUTHORIZED, request);
            return;
        } catch (SignatureException | MalformedJwtException ex) {
            handleException(response, "올바르지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED, request);
            return;
        } catch (Exception ex) {
            handleException(response, "사용자 인증에 실패했습니다", HttpStatus.UNAUTHORIZED, request);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
