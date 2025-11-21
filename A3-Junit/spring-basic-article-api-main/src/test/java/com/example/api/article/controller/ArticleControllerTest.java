package com.example.api.article.article;

import com.example.api.article.article.dto.ArticleCreateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ArticleService articleService;

    @Test
    void deveCriarArtigo_comSucesso_retorna201() throws Exception {
        // dado
        ArticleCreateDTO dto = new ArticleCreateDTO();
        dto.setTitle("Meu primeiro artigo");
        dto.setDescription("descrição legal");
        dto.setBody("conteúdo aqui");
        dto.setTags(List.of("java", "spring"));

        Article artigoSalvo = new Article();
        artigoSalvo.setId(99L);
        artigoSalvo.setTitle(dto.getTitle());

        when(articleService.create(any(ArticleCreateDTO.class))).thenReturn(artigoSalvo);

        // quando + então
        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.title", is("Meu primeiro artigo")));
    }

    @Test
    void deveRetornar400_quandoTitleForNulo() throws Exception {
        ArticleCreateDTO dto = new ArticleCreateDTO();
        // title fica nulo de propósito

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}