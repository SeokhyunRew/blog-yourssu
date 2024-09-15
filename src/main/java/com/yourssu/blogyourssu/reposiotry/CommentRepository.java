package com.yourssu.blogyourssu.reposiotry;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {


}
