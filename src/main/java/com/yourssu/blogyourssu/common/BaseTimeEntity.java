package com.yourssu.blogyourssu.common;/*
 * created by seokhyun on 2024-09-14.
 */

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //자동 시간 매핑
public abstract class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime create_at;

    @LastModifiedDate
    private LocalDateTime updated_at;


}