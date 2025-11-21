package com.example.api.article.tag;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}