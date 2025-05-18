package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table
@Data
@Builder
public class Article {
    @Id
    private Long id;
    private String title;
    private String description;
    private Date created;
    private Date updated;
    @Column("AUTHOR")
    private Long memberId;
}
