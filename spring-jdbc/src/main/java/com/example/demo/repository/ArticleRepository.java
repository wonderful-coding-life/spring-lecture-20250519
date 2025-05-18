package com.example.demo.repository;

import com.example.demo.model.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    List<Article> findByMemberId(Long id);
    List<Article> findByTitleContaining(String keyword);
}
