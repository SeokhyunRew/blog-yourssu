package com.yourssu.blogyourssu.controller;
/*
 * created by seokhyun on 2024-09-16.
 */


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import com.yourssu.blogyourssu.dto.request.CommentRequest;
import com.yourssu.blogyourssu.dto.response.CommentResponse;
import com.yourssu.blogyourssu.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value="/article/{articleId}/comment")
    public ResponseEntity<CommentResponse> createComment(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @RequestBody @Valid CommentRequest request,
                                                         @PathVariable Long articleId){
        CommentResponse commentResponse = commentService.createComment(request, userId, articleId);

        return ResponseEntity
                .status(CREATED)
                .body(commentResponse);
    }

    @PutMapping(value="/comment/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @RequestBody @Valid CommentRequest request,
                                                         @PathVariable Long commentId){
        CommentResponse commentResponse = commentService.updateComment(commentId, userId, request );

        return ResponseEntity
                .status(OK)
                .body(commentResponse);
    }

    @DeleteMapping(value="/comment/{commentId}")
    public void deleteComment(@AuthenticationPrincipal(expression = "userId")Long userId,
                              @PathVariable Long commentId){
        commentService.deleteById(commentId, userId);
    }
}
