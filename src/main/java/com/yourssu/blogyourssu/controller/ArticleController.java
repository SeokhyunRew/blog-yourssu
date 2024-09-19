package com.yourssu.blogyourssu.controller;/*
 * created by seokhyun on 2024-09-16.
 */

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import com.yourssu.blogyourssu.dto.request.ArticleRequest;
import com.yourssu.blogyourssu.dto.response.ArticleResponse;
import com.yourssu.blogyourssu.service.ArticleService;
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
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "게시글 생성", description = "헤더 Authorization에 토큰값 넣어야함")
    @PostMapping(value="/article")
    public ResponseEntity<ArticleResponse> createArticle(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @RequestBody @Valid ArticleRequest request){
        ArticleResponse articleResponse = articleService.createArticle(request,userId );

        return ResponseEntity
                .status(CREATED)
                .body(articleResponse);
    }

    @Operation(summary = "게시글 수정", description = "수정할 게시글 articleId 요청에 넣어야함, 헤더 Authorization에 토큰값 넣어야함")
    @PutMapping(value="/article/{articleId}")
    public ResponseEntity<ArticleResponse> updateArticle(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @RequestBody @Valid ArticleRequest request,
                                                         @PathVariable Long articleId){
        ArticleResponse articleResponse = articleService.updateArticle(articleId, userId, request );

        return ResponseEntity
                .status(OK)
                .body(articleResponse);
    }

    @Operation(summary = "게시글 삭제", description = "삭제할 게시글 articleId 요청에 넣어야함, 헤더 Authorization에 토큰값 넣어야함")
    @DeleteMapping(value="/article/{articleId}")
    public void deleteArticle(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @PathVariable Long articleId){
        articleService.deleteById(articleId, userId);
    }
}
