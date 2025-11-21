package com.example.api.article.article;

import com.example.api.article.tag.Tag;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToMany
    private List<Tag> tags;
}
