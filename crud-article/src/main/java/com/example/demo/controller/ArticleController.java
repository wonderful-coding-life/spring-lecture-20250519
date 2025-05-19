package com.example.demo.controller;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    //@GetMapping
    public List<ArticleResponse> getAll(@RequestParam(name= "memberId", required=false) Long memberId) {
        if (memberId == null) {
            return articleService.findAll();
        } else {
            return articleService.findByMemberId(memberId);
        }
    }

    // http://localhost:8080/api/articles?page=1&size=3&sort=title,asc&sort=description,desc
    @GetMapping
    public Page<ArticleResponse> getAllWithPageable(@PageableDefault(page=0, size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable, @RequestParam(name= "memberId", required=false) Long memberId) {
        if (memberId == null) {
            return articleService.findAll(pageable);
        } else {
            return articleService.findByMemberId(memberId, pageable);
        }
    }

    @GetMapping("/{id}")
    public ArticleResponse get(@PathVariable("id") Long id) {
        return articleService.findById(id);
    }

    @PutMapping("/{id}")
    public ArticleResponse put(@PathVariable("id") Long id, @RequestBody ArticleRequest articleRequest) {
        return articleService.update(id, articleRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        articleService.delete(id);
    }
}
