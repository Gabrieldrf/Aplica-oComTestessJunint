package com.example.api.article.tag;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // rollback automático no final de cada teste
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    void deveListarTodasAsTags() {
        Tag t1 = new Tag();
        t1.setName("java");
        Tag t2 = new Tag();
        t2.setName("spring");

        tagService.save(t1);
        tagService.save(t2);

        List<Tag> todas = tagService.findAll();

        assertThat(todas).hasSize(2);
        assertThat(todas).extracting(Tag::getName)
                .containsExactlyInAnyOrder("java", "spring");
    }

    @Test
    void deveCriarUmaNovaTag() {
        Tag tag = new Tag();
        tag.setName("docker");

        Tag salva = tagService.save(tag);

        assertThat(salva.getId()).isNotNull();
        assertThat(salva.getName()).isEqualTo("docker");
    }

    @Test
    void devePermitirCriarTagsComMesmoNome() {
        // Comportamento atual do sistema: NÃO evita duplicatas
        Tag tag1 = new Tag();
        tag1.setName("java");
        Tag tag2 = new Tag();
        tag2.setName("java");

        tagService.save(tag1);
        tagService.save(tag2);

        List<Tag> todas = tagService.findAll();

        assertThat(todas).hasSize(2); // passa com o código atual
        assertThat(todas).allMatch(t -> t.getName().equals("java"));
    }

    @Test
    void deveDeletarTagPorId() {
        Tag tag = new Tag();
        tag.setName("typescript");
        Tag salva = tagService.save(tag);

        tagService.delete(salva.getId());

        assertThat(tagService.findAll()).isEmpty();
    }
}