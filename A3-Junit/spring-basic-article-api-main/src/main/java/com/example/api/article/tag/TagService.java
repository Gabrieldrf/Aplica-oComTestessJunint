package com.example.api.article.tag;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository repository;

    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public List<Tag> findAll() {
        return repository.findAll();
    }

    public Tag save(Tag tag) {
        return repository.save(tag);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}