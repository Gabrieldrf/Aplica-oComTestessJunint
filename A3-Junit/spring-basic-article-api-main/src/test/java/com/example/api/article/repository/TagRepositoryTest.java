package com.example.api.article.tag;

import com.example.api.article.article.Article;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByName_deveRetornarTagQuandoExiste() {
        Tag java = new Tag();
        java.setName("java");
        tagRepository.save(java);
        entityManager.flush();
        entityManager.clear();

        var encontrada = tagRepository.findByName("java");

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getName()).isEqualTo("java");
        assertThat(encontrada.get().getId()).isNotNull();
    }

    @Test
    void findByName_deveRetornarEmptyQuandoNaoExiste() {
        var encontrada = tagRepository.findByName("kotlin");

        assertThat(encontrada).isEmpty();
    }

    @Test
    void devePermitirTagsComMesmoNome_masServiceDeveEvitarIsso() {
        // O banco permite (não tem unique), vamos provar
        Tag t1 = new Tag(); t1.setName("spring");
        Tag t2 = new Tag(); t2.setName("spring");

        tagRepository.save(t1);
        tagRepository.save(t2);
        entityManager.flush();

        var todas = tagRepository.findAll();
        assertThat(todas).hasSize(2);
        assertThat(todas).extracting(Tag::getName).containsOnly("spring");
    }

    @Test
    void tagDeveSerReutilizadaCorretamenteEmVariosArticles_quandoUsamosFindByName() {
        // Simula exatamente o que o ArticleService faz

        // Primeiro artigo cria a tag "docker"
        Tag docker = tagRepository.findByName("docker")
                .orElseGet(() -> {
                    Tag nova = new Tag();
                    nova.setName("docker");
                    return tagRepository.save(nova);
                });

        Article a1 = new Article();
        a1.setTitle("Deploy com Docker");
        a1.setDescription("desc");
        a1.setBody("body");
        a1.setTags(List.of(docker));
        entityManager.persist(a1);

        // Segundo artigo tenta usar a mesma tag
        Tag mesmaTag = tagRepository.findByName("docker")
                .orElseGet(() -> {
                    Tag nova = new Tag();
                    nova.setName("docker");
                    return tagRepository.save(nova);
                });

        Article a2 = new Article();
        a2.setTitle("Docker Compose");
        a2.setTags(List.of(mesmaTag));
        entityManager.persist(a2);

        entityManager.flush();
        entityManager.clear();

        // Agora verifica: só existe UMA tag "docker" no banco
        var todasDocker = tagRepository.findAll().stream()
                .filter(t -> "docker".equals(t.getName()))
                .toList();

        assertThat(todasDocker).hasSize(1);  // ← ESSA É A PROVA DE OURO
        assertThat(todasDocker.get(0).getId()).isEqualTo(docker.getId());

        // E os dois artigos estão usando a mesma instância
        Article encontrado1 = entityManager.find(Article.class, a1.getId());
        Article encontrado2 = entityManager.find(Article.class, a2.getId());

        assertThat(encontrado1.getTags().get(0).getId())
                .isEqualTo(encontrado2.getTags().get(0).getId());
    }
}