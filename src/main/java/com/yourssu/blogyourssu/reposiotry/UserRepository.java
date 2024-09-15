package com.yourssu.blogyourssu.reposiotry;/*
 * created by seokhyun on 2024-09-15.
 */

import com.yourssu.blogyourssu.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {


}
