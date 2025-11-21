package com.example.api.article.article;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.api.article.article.dto.ArticleCreateDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<Article> create(@Valid @RequestBody ArticleCreateDTO dto) {
        Article saved = articleService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}