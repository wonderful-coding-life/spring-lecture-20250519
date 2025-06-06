package com.example.demo.repository;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    public int deleteByMember(Member member);
}
