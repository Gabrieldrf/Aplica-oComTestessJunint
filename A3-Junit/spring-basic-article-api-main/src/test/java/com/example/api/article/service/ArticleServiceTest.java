package com.example.api.article.article;

import com.example.api.article.article.dto.ArticleCreateDTO;
import com.example.api.article.tag.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // faz rollback no final
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void deveCriarArtigoComTags() {
        ArticleCreateDTO dto = new ArticleCreateDTO();
        dto.setTitle("Teste com Tags");
        dto.setDescription("Descrição teste");
        dto.setBody("Conteúdo do artigo aqui");
        dto.setTags(List.of("java", "spring", "docker"));

        Article artigoSalvo = articleService.create(dto);

        assertThat(artigoSalvo).isNotNull();
        assertThat(artigoSalvo.getId()).isNotNull();
        assertThat(artigoSalvo.getTitle()).isEqualTo("Teste com Tags");
        assertThat(artigoSalvo.getTags()).hasSize(3);
        assertThat(artigoSalvo.getTags())
            .extracting(Tag::getName)
            .containsExactlyInAnyOrder("java", "spring", "docker");
    }

    @Test
    void devePermitirTagsRepetidasNoDTO() {
        // Este teste passa com o seu código atual (sem Set, sem deduplicação)
        ArticleCreateDTO dto = new ArticleCreateDTO();
        dto.setTitle("Java Java Java");
        dto.setBody("Corpo");
        dto.setTags(List.of("java", "java", "java"));

        Article artigo = articleService.create(dto);

        assertThat(artigo.getTags()).hasSize(3); // ← passa com o código atual
        assertThat(artigo.getTags())
            .allMatch(tag -> tag.getName().equals("java"));
    }

    @Test
    void deveCriarTagsDiferentesCorretamente() {
        ArticleCreateDTO dto = new ArticleCreateDTO();
        dto.setTitle("Várias techs");
        dto.setBody("...");
        dto.setTags(List.of("react", "node", "postgresql"));

        Article artigo = articleService.create(dto);

        assertThat(artigo.getTags()).hasSize(3);
    }
}