package com.yourssu.blogyourssu.domain;/*
 * created by seokhyun on 2024-09-14.
 */

import com.yourssu.blogyourssu.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntity extends BaseTimeEntity {
    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String content;

    @NotBlank
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
