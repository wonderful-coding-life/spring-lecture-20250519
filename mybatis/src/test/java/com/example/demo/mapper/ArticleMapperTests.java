package com.example.demo.mapper;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:/test-article.sql"})
public class ArticleMapperTests {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void selectAll() {
        List<Article> articles = articleMapper.selectAll();
        log.info("selectAll size {}", articles.size());
        for (Article article : articles) {
            log.info("{} ", article);
        }
    }

    @Test
    public void selectById() {
        Article article = articleMapper.selectById(1L).orElseThrow();
        log.info("{}", article);
    }

    @Test
    public void selectByMemberId() {
        Member member = memberMapper.selectByEmail("SeojunYoon@hanbit.co.kr").orElseThrow();
        List<Article> articles = articleMapper.selectByMemberId(member.getId());
        log.info("selectAll size {}", articles.size());
        for (Article article : articles) {
            log.info("{} ", article);
        }
    }

    @Test
    public void insert() {
        Member member = memberMapper.selectByEmail("MiyeongKong@hanbit.co.kr").orElseThrow();
        Article article = Article.builder()
                .title("방학 첫날")
                .description("신난다. 방학이다.")
                .memberId(member.getId()).build();
        int insertedCount = articleMapper.insert(article);
        log.info("insert {}", insertedCount);
        log.info("inserted {}", article);
        log.info("inserted {}", articleMapper.selectById(article.getId()));
    }

    @Test
    public void update() throws InterruptedException {
        Thread.sleep(2000);
        Article article = articleMapper.selectById(1L).orElseThrow();
        int updatedCount = articleMapper.update(article.getId(), "방학 둘째날", "신난다. 어제부터 방학이다.");
        log.info("updatedCount {}", updatedCount);
        article = articleMapper.selectById(article.getId()).orElseThrow();
        log.info("selectById {}", article);
    }

    @Test
    public void delete() {
        articleMapper.deleteById(1L);
        log.info("{}", articleMapper.selectAllCount());

        // 윤광철
        articleMapper.deleteByMemberId(2L);
        log.info("{}", articleMapper.selectAllCount());

        articleMapper.deleteAll();
        log.info("{}", articleMapper.selectAllCount());
    }
}
