package com.example.demo.repository;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByMember(Member member);
    Page<Article> findAllByMember(Member member, Pageable pageable);
}
