package com.example.demo.repository;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Transactional
    void deleteAllByMember(Member member);
}
