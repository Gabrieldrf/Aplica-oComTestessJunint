package com.example.api.article.repository;

import com.example.api.article.article.Article;
import com.example.api.article.article.ArticleRepository;
import com.example.api.article.tag.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void deveSalvarArtigoComTagsNovas() {
        Tag java = new Tag();
        java.setName("java");
        entityManager.persist(java);

        Tag spring = new Tag();
        spring.setName("spring");
        entityManager.persist(spring);

        entityManager.flush(); // garante que as tags tenham ID

        Article artigo = new Article();
        artigo.setTitle("Spring Boot com JPA");
        artigo.setDescription("Testando ManyToMany");
        artigo.setBody("Corpo do artigo...");
        artigo.setTags(List.of(java, spring));

        Article salvo = articleRepository.save(artigo);
        entityManager.flush();
        entityManager.clear();

        Article encontrado = articleRepository.findById(salvo.getId()).orElseThrow();

        assertThat(encontrado.getTitle()).isEqualTo("Spring Boot com JPA");
        assertThat(encontrado.getTags()).hasSize(2);
        assertThat(encontrado.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("java", "spring");
    }

    @Test
    void deveReutilizarTagsExistentesAoSalvarArtigo() {
        Tag docker = new Tag();
        docker.setName("docker");
        entityManager.persistAndFlush(docker);

        entityManager.clear();

        Article artigo = new Article();
        artigo.setTitle("Deploy com Docker");
        artigo.setTags(List.of(docker));

        Article salvo = articleRepository.save(artigo);
        entityManager.flush();
        entityManager.clear();

        Article encontrado = articleRepository.findById(salvo.getId()).orElseThrow();
        assertThat(encontrado.getTags()).hasSize(1);
        assertThat(encontrado.getTags().get(0).getId()).isEqualTo(docker.getId());

        Long count = entityManager.getEntityManager()
                .createQuery("SELECT COUNT(t) FROM Tag t WHERE t.name = 'docker'", Long.class)
                .getSingleResult();
        assertThat(count).isEqualTo(1L);
    }

    @Test
    void deveBuscarArtigoComTagsCarregadasCorretamente() {
        Tag react = new Tag(); react.setName("react");
        Tag nodejs = new Tag(); nodejs.setName("nodejs");

        entityManager.persist(react);
        entityManager.persist(nodejs);
        entityManager.flush();

        Article artigo = new Article();
        artigo.setTitle("Fullstack JS");
        artigo.setTags(List.of(react, nodejs));
        entityManager.persistAndFlush(artigo);

        entityManager.clear();

        Article encontrado = articleRepository.findById(artigo.getId()).orElseThrow();

        assertThat(encontrado.getTags()).hasSize(2);
        assertThat(encontrado.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("react", "nodejs");
    }
}