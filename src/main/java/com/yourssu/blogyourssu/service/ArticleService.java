package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.common.exception.customexception.ForbiddenException;
import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.ArticleRequest;
import com.yourssu.blogyourssu.dto.response.ArticleResponse;
import com.yourssu.blogyourssu.dto.util.ArticleDtoUtil;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
import com.yourssu.blogyourssu.reposiotry.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleSearchService articleSearchService;
    private final UserSearchService userSearchService;
    private final CommentRepository commentRepository;

    public ArticleResponse createArticle(ArticleRequest request, Long userId){
        UserEntity user = userSearchService.findById(userId);
        ArticleEntity saveArticle = articleRepository.save(request.toEntity(user));;

        return ArticleDtoUtil.articleToArticleResponse(saveArticle) ;
    }

    public ArticleResponse updateArticle(Long articleId, Long userId, ArticleRequest request){
        ArticleEntity findArticle = articleSearchService.findById(articleId);

        if (!findArticle.getUserEntity().getId().equals(userId)) {
            throw new ForbiddenException("작성자만 수정 가능합니다.");
        }

        findArticle.update(request.getTitle(), request.getContent());

        return ArticleDtoUtil.articleToArticleResponse(findArticle);
    }

    public void deleteById(Long articleId, Long userId){
        ArticleEntity findArticle = articleSearchService.findById(articleId);

        if (!findArticle.getUserEntity().getId().equals(userId)) {
            throw new ForbiddenException("작성자만 삭제 가능합니다.");
        }

        commentRepository.deleteByArticleId(articleId);
        articleRepository.deleteById(articleId);
    }
}
