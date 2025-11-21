package com.example.api.article.article.dto;

import lombok.Data;
import java.util.List;

import jakarta.validation.constraints.NotNull;

@Data
public class ArticleCreateDTO {

    @NotNull(message = "title nao deve ser nulo")
    private String title;
    private String description;
    private String body;
    private List<String> tags;
}
