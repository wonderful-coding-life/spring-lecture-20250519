package com.example.demo.repository;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:/test-article.sql"})
public class ArticleRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void findById() {
        Article article = articleRepository.findById(1L).orElseThrow();
        log.info("{}", article);
    }

    @Test
    public void findAll() {
        List<Article> articles = articleRepository.findAll();
        log.info("{}", articles);
    }

    @Test
    public void update() throws InterruptedException {
        Article article = articleRepository.findById(1L).orElseThrow();
        article.setTitle("방학 세째날");
        article.setDescription("즐거운 방학 세째날이다");
        Thread.sleep(2000);

        //article.getMember().setAge(11);
        articleRepository.save(article); // child is not updated
        //memberRepository.save(article.getMember()); // need to update separately

        log.info("{}", articleRepository.findById(1L));
    }

    @Test
    @Transactional
    public void deleteByMember() {
        Member member = memberRepository.findById(1L).orElseThrow(); // 윤서준
        int deleted = articleRepository.deleteByMember(member);
        log.info("deleted {}", deleted);
    }
}
