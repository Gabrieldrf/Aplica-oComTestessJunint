package com.example.api.article.controller;

import com.example.api.article.tag.Tag;
import com.example.api.article.tag.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(com.example.api.article.tag.TagController.class)
class TagControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TagService service;

    @Test
    void deveListarTodasAsTags() throws Exception {
        Tag java = new Tag(); java.setId(1L); java.setName("java");
        Tag spring = new Tag(); spring.setId(2L); spring.setName("spring");

        when(service.findAll()).thenReturn(List.of(java, spring));

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("java")))
                .andExpect(jsonPath("$[1].name", is("spring")));
    }

    @Test
    void deveCriarNovaTag_comSucesso() throws Exception {
        Tag nova = new Tag();
        nova.setName("kubernetes");

        Tag salva = new Tag();
        salva.setId(99L);
        salva.setName("kubernetes");

        when(service.save(any(Tag.class))).thenReturn(salva);

        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nova)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.name", is("kubernetes")));
    }

    @Test
    void deveDeletarTag() throws Exception {
        Long id = 7L;
        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/tags/{id}", id))
                .andExpect(status().isOk());

        verify(service).delete(id);
    }

    @Test
    void deveAceitarTagSemNome() throws Exception {
        Tag tagSemNome = new Tag();

        when(service.save(any(Tag.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagSemNome)))
                .andExpect(status().isOk());
    }
}