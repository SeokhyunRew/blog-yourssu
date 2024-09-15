package com.yourssu.blogyourssu.dto.response;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.ArticleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;

    public ArticleResponse(ArticleEntity articleEntity) {
        id = articleEntity.getId();
        title = articleEntity.getTitle();
        content = articleEntity.getContent();
    }
}
