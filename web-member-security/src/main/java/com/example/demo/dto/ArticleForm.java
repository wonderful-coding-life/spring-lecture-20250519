package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleForm {
    private Long id;
    @NotBlank(message = "게시글 제목을 입력하세요")
    private String title;
    @NotBlank(message = "게시글 내용을 입력하세요")
    private String description;
}
