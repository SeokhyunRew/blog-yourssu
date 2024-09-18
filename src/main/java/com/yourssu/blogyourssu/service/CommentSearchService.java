package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.common.exception.customexception.NotFoundException;
import com.yourssu.blogyourssu.domain.CommentEntity;
import com.yourssu.blogyourssu.reposiotry.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentSearchService {

    private final CommentRepository commentRepository;

    public CommentEntity findById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(()-> new NotFoundException(commentId + "에 해당하는 게시글이 없습니다."));
    }
}
