package com.yourssu.blogyourssu.controller;/*
 * created by seokhyun on 2024-09-16.
 */

import static org.springframework.http.HttpStatus.CREATED;

import com.yourssu.blogyourssu.dto.request.ArticleRequest;
import com.yourssu.blogyourssu.dto.response.ArticleResponse;
import com.yourssu.blogyourssu.service.ArticleService;
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

    @PostMapping(value="/article")
    public ResponseEntity<ArticleResponse> createArticle(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @RequestBody ArticleRequest request){
        ArticleResponse articleResponse = articleService.createArticle(request,userId );

        return ResponseEntity
                .status(CREATED)
                .body(articleResponse);
    }

    @PutMapping(value="/article/{articleId}")
    public ResponseEntity<ArticleResponse> updateArticle(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @RequestBody ArticleRequest request,
                                                         @PathVariable Long articleId){
        ArticleResponse articleResponse = articleService.updateArticle(articleId, userId, request );

        return ResponseEntity
                .status(CREATED)
                .body(articleResponse);
    }

    @DeleteMapping(value="/article/{articleId}")
    public void deleteArticle(@AuthenticationPrincipal(expression = "userId")Long userId,
                                                         @PathVariable Long articleId){
        articleService.deleteById(articleId, userId);
    }
}
