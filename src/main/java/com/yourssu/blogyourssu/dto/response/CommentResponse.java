package com.yourssu.blogyourssu.dto.response;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.CommentEntity;
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
public class CommentResponse {
    private Long id;
    private String content;

    public CommentResponse(CommentEntity commentEntity) {
        id = commentEntity.getId();
        content = commentEntity.getContent();
    }
}
