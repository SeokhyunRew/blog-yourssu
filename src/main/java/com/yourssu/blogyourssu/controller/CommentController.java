package com.yourssu.blogyourssu.controller;
/*
 * created by seokhyun on 2024-09-16.
 */


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import com.yourssu.blogyourssu.dto.request.CommentRequest;
import com.yourssu.blogyourssu.dto.response.CommentResponse;
import com.yourssu.blogyourssu.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "댓글 생성", description = "댓글 달고싶은 articleId 요청에 넣어야함, 헤더 Authorization에 토큰값 넣어야함")
    @PostMapping(value="/article/{articleId}/comment")
    public ResponseEntity<CommentResponse> createComment(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @RequestBody @Valid CommentRequest request,
                                                         @PathVariable Long articleId){
        CommentResponse commentResponse = commentService.createComment(request, userId, articleId);

        return ResponseEntity
                .status(CREATED)
                .body(commentResponse);
    }

    @Operation(summary = "댓글 수정", description = "수정하고 싶은 댓글 commentId 요청에 넣어야함, 헤더 Authorization에 토큰값 넣어야함")
    @PutMapping(value="/comment/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @RequestBody @Valid CommentRequest request,
                                                         @PathVariable Long commentId){
        CommentResponse commentResponse = commentService.updateComment(commentId, userId, request );

        return ResponseEntity
                .status(OK)
                .body(commentResponse);
    }

    @Operation(summary = "댓글 삭제", description = "삭제하고 싶은 댓글 commentId 요청에 넣어야함, 헤더 Authorization에 토큰값 넣어야함")
    @DeleteMapping(value="/comment/{commentId}")
    public void deleteComment(@AuthenticationPrincipal(expression = "userId")Long userId,
                              @PathVariable Long commentId){
        commentService.deleteById(commentId, userId);
    }
}
