package com.yourssu.blogyourssu.service;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.common.exception.customexception.ForbiddenException;
import com.yourssu.blogyourssu.domain.ArticleEntity;
import com.yourssu.blogyourssu.domain.CommentEntity;
import com.yourssu.blogyourssu.domain.UserEntity;
import com.yourssu.blogyourssu.dto.request.CommentRequest;
import com.yourssu.blogyourssu.dto.response.CommentResponse;
import com.yourssu.blogyourssu.dto.util.CommentDtoUtil;
import com.yourssu.blogyourssu.reposiotry.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentSearchService commentSearchService;
    private final ArticleSearchService articleSearchService;
    private final UserSearchService userSearchService;

    public CommentResponse createComment(CommentRequest request, Long userId, Long articleId){
        ArticleEntity findArticle = articleSearchService.findById(articleId);
        UserEntity findUser = userSearchService.findById(userId);

        CommentEntity saveComment = commentRepository.save(request.toEntity(findArticle, findUser));

        return CommentDtoUtil.commentToCommentResponse(saveComment) ;
    }

    public CommentResponse updateComment(Long commentId, Long userId, CommentRequest request){
        CommentEntity findComment = commentSearchService.findById(commentId);

        if (!findComment.getUserEntity().getId().equals(userId)) {
            throw new ForbiddenException("작성자만 수정 가능합니다.");
        }

        findComment.update(request.getContent());

        return CommentDtoUtil.commentToCommentResponse(findComment);
    }

    public void deleteById(Long commentId, Long userId){
        CommentEntity findComment = commentSearchService.findById(commentId);

        if (!findComment.getUserEntity().getId().equals(userId)) {
            throw new ForbiddenException("작성자만 삭제 가능합니다.");
        }

        commentRepository.deleteById(commentId);
    }
}
