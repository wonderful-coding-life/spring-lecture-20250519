package com.example.demo.service;

import com.example.demo.dto.ArticleDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:/test-article.sql"})
public class ArticleServiceTests {
    @Autowired
    private ArticleService articleService;

    @Test
    public void getArticleById() {
        ArticleDto articleDto = articleService.getArticleById(1L);
        log.info("{}", articleDto);
    }

    @Test
    public void getArticleAll() {
        List<ArticleDto> articleDtos = articleService.getArticleAll();
        log.info("size {}", articleDtos.size());
        for (ArticleDto articleDto : articleDtos) {
            log.info("{}", articleDto);
        }
    }

    @Test
    public void getArticleByMemberId() {
        List<ArticleDto> articleDtos = articleService.getArticleByMemberId(1L);
        log.info("size {}", articleDtos.size());
        for (ArticleDto articleDto : articleDtos) {
            log.info("{}", articleDto);
        }
    }
}
