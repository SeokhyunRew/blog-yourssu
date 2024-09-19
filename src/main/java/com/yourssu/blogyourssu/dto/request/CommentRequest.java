package com.yourssu.blogyourssu.dto.request;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.CommentEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentRequest {
    String content;
    public CommentEntity toEntity(ArticleEntity articleEntity, UserEntity userEntity){
        return CommentEntity.builder()
                .content(content)
                .articleEntity(articleEntity)
                .userEntity(userEntity)
                .build();
    }
}