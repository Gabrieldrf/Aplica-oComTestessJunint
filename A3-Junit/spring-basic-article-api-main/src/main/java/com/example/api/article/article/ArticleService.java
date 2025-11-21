package com.example.api.article.article;

import com.example.api.article.article.dto.ArticleCreateDTO;
import com.example.api.article.tag.Tag;
import com.example.api.article.tag.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;

    public ArticleService(ArticleRepository articleRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
    }

    public Article create(ArticleCreateDTO dto) {

        List<Tag> processedTags = dto.getTags().stream()
                .map(name ->
                        tagRepository.findByName(name)
                                .orElseGet(() -> {
                                    Tag t = new Tag();
                                    t.setName(name);
                                    return tagRepository.save(t);
                                })
                )
                .collect(Collectors.toList());

        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setBody(dto.getBody());
        article.setTags(processedTags);

        return articleRepository.save(article);
    }
}
