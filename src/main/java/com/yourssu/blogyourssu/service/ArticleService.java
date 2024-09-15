package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.ArticleRequest;
import com.yourssu.blogyourssu.dto.response.ArticleResponse;
import com.yourssu.blogyourssu.dto.util.ArticleDtoUtil;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleSearchService articleSearchService;


    public ArticleResponse createArticle(ArticleRequest request, UserEntity userEntity){
        ArticleEntity saveArticle = articleRepository.save(request.toEntity(userEntity));

        return ArticleDtoUtil.articleToArticleResponse(saveArticle) ;
    }

    public ArticleResponse updateArticle(Long articleId, UserEntity userEntity, ArticleRequest request){
        ArticleEntity findArticle = articleSearchService.findById(articleId);

        if (!findArticle.getUserEntity().getId().equals(userEntity.getId())) {
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }

        findArticle.update(request.getTitle(), request.getContent());

        return ArticleDtoUtil.articleToArticleResponse(findArticle);
    }

    public void deleteById(Long articleId, UserEntity userEntity){
        ArticleEntity findArticle = articleSearchService.findById(articleId);

        if (!findArticle.getUserEntity().getId().equals(userEntity.getId())) {
            throw new IllegalArgumentException("작성자만 삭제 가능합니다.");
        }
        articleRepository.deleteById(articleId);
    }
}
