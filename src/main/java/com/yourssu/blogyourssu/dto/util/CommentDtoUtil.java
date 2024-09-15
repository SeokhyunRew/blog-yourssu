package com.yourssu.blogyourssu.dto.util;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.CommentEntity;
import com.yourssu.blogyourssu.dto.response.CommentResponse;

public class CommentDtoUtil {
    public static CommentResponse commentToCommentResponse(CommentEntity commentEntity){
        return  CommentResponse.builder()
                .id(commentEntity.getId())
                .content(commentEntity.getContent())
                .build();
    }
}
