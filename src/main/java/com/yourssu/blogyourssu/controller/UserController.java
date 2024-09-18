package com.yourssu.blogyourssu.controller;/*
 * created by seokhyun on 2024-09-16.
 */

import static org.springframework.http.HttpStatus.CREATED;

import com.yourssu.blogyourssu.dto.request.UserRequest;
import com.yourssu.blogyourssu.dto.response.UserResponse;
import com.yourssu.blogyourssu.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping(value="/all/users")
    public ResponseEntity<UserResponse> createAccount(@RequestBody @Valid UserRequest request){
        UserResponse userResponse = userService.createAccount(request);

        return ResponseEntity
                .status(CREATED)
                .body(userResponse);
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal(expression = "userId")Long userId) {
        userService.delete(userId);

        return ResponseEntity
                .noContent()
                .build();
    }

}
