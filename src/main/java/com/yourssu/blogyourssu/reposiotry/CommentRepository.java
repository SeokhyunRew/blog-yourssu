package com.yourssu.blogyourssu.reposiotry;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Modifying
    @Query(value = "DELETE FROM comment_entity WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM comment_entity WHERE article_id = :articleId", nativeQuery = true)
    void deleteByArticleId(@Param("articleId") Long articleId);

    @Modifying
    @Query(value = "DELETE FROM comment_entity WHERE article_id IN (SELECT article_id FROM article_entity WHERE user_id = :userId)", nativeQuery = true)
    void deleteByArticleIdUseUserId(@Param("userId") Long userId);
}
