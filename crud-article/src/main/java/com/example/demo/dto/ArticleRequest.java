package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ArticleRequest {
    private String title;
    private String description;
}
