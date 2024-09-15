package com.yourssu.blogyourssu.dto.request;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ArticleRequest {
    private String title;
    private String content;

    public ArticleEntity toEntity(UserEntity userEntity) {	// 생성자를 사용해 객체 생성
        return ArticleEntity.builder()
                .title(title)
                .content(content)
                .userEntity(userEntity)
                .build();
    }
}
