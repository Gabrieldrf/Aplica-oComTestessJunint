package com.example.api.article.controller;

import com.example.api.article.user.User;
import com.example.api.article.user.UserService;
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

@WebMvcTest(com.example.api.article.user.UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService service;

    @Test
    void deveListarTodosOsUsers() throws Exception {
        User user1 = new User(); user1.setId(1L); user1.setUsername("gabriel");
        User user2 = new User(); user2.setId(2L); user2.setUsername("maria");

        when(service.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("gabriel")))
                .andExpect(jsonPath("$[1].username", is("maria")));
    }

    @Test
    void deveBuscarUserPorId() throws Exception {
        User user = new User();
        user.setId(10L);
        user.setUsername("joao");
        user.setEmail("joao@email.com");

        when(service.findById(10L)).thenReturn(user);

        mockMvc.perform(get("/users/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.username", is("joao")))
                .andExpect(jsonPath("$.email", is("joao@email.com")));
    }

    @Test
    void deveCriarNovoUser() throws Exception {
        User novo = new User();
        novo.setUsername("ana");
        novo.setEmail("ana@teste.com");
        novo.setPassword("123456");

        User salvo = new User();
        salvo.setId(99L);
        salvo.setUsername("ana");
        salvo.setEmail("ana@teste.com");
        salvo.setPassword("123456");

        when(service.save(any(User.class))).thenReturn(salvo);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.username", is("ana")));
    }

    @Test
    void deveAtualizarUserExistente() throws Exception {
        User atualizado = new User();
        atualizado.setUsername("pedro_atualizado");
        atualizado.setEmail("pedro@novo.com");

        User retorno = new User();
        retorno.setId(5L);
        retorno.setUsername("pedro_atualizado");
        retorno.setEmail("pedro@novo.com");

        when(service.save(any(User.class))).thenReturn(retorno);

        mockMvc.perform(put("/users/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.username", is("pedro_atualizado")));
    }

    @Test
    void deveDeletarUser() throws Exception {
        Long id = 7L;
        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isOk());

        verify(service).delete(id);
    }
}