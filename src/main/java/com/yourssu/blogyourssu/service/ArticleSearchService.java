package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.common.exception.customexception.NotFoundException;
import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.reposiotry.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleSearchService {

    private final ArticleRepository articleRepository;

    public ArticleEntity findById(Long articleId){
        return articleRepository.findById(articleId).orElseThrow(()-> new NotFoundException(articleId + "에 해당하는 게시글이 없습니다."));
    }
}
