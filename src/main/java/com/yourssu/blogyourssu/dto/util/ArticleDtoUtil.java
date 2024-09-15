package com.yourssu.blogyourssu.dto.util;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.dto.response.ArticleResponse;

public class ArticleDtoUtil {
    public static ArticleResponse articleToArticleResponse(ArticleEntity articleEntity){
        return  ArticleResponse.builder()
                .id(articleEntity.getId())
                .title(articleEntity.getTitle())
                .content(articleEntity.getContent())
                .build();
    }
}
