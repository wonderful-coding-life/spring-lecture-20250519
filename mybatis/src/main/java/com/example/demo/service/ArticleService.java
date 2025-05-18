package com.example.demo.service;

import com.example.demo.dto.ArticleDto;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.model.Article;
import com.example.demo.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final MemberMapper memberMapper;
    private final ArticleMapper articleMapper;

    public ArticleDto getArticleById(Long id) {
        Article article = articleMapper.selectById(id).orElseThrow();
        return mapToArticleDto(article);
    }

    public List<ArticleDto> getArticleAll() {
        return articleMapper.selectAll().stream().map(this::mapToArticleDto).toList();
    }

    public List<ArticleDto> getArticleByMemberId(Long memberId) {
        return articleMapper.selectByMemberId(memberId).stream().map(this::mapToArticleDto).toList();
    }

    private ArticleDto mapToArticleDto(Article article) {
        Member member = memberMapper.selectById(article.getMemberId()).orElseThrow();
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .name(member.getName())
                .email(member.getEmail())
                .updated(article.getUpdated()).build();
    }
}
