package com.example.api.article.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void deveListarTodosOsUsers() {
        User u1 = new User();
        u1.setUsername("gabriel");
        u1.setEmail("gabriel@test.com");
        u1.setPassword("123");

        User u2 = new User();
        u2.setUsername("maria");
        u2.setEmail("maria@test.com");
        u2.setPassword("456");

        userService.save(u1);
        userService.save(u2);

        List<User> todos = userService.findAll();

        assertThat(todos).hasSize(2);
        assertThat(todos).extracting(User::getUsername)
                .containsExactlyInAnyOrder("gabriel", "maria");
    }

    @Test
    void deveBuscarUserPorId() {
        User user = new User();
        user.setUsername("joao");
        user.setEmail("joao@email.com");
        user.setPassword("senha123");

        User salvo = userService.save(user);
        User encontrado = userService.findById(salvo.getId());

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getUsername()).isEqualTo("joao");
    }

    @Test
    void deveCriarNovoUser() {
        User novo = new User();
        novo.setUsername("ana");
        novo.setEmail("ana@teste.com");
        novo.setPassword("123456");

        User salvo = userService.save(novo);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getUsername()).isEqualTo("ana");
    }

    @Test
    void deveAtualizarUserExistente() {
        User original = new User();
        original.setUsername("pedro");
        original.setEmail("pedro@velho.com");
        original.setPassword("123");

        User salvo = userService.save(original);

        User atualizado = new User();
        atualizado.setId(salvo.getId());
        atualizado.setUsername("pedro_novo");
        atualizado.setEmail("pedro@novo.com");
        atualizado.setPassword("nova123");

        User resultado = userService.save(atualizado);

        assertThat(resultado.getUsername()).isEqualTo("pedro_novo");
        assertThat(resultado.getEmail()).isEqualTo("pedro@novo.com");
    }

    @Test
    void deveDeletarUserPorId() {
        User user = new User();
        user.setUsername("deleteme");
        user.setEmail("delete@test.com");
        user.setPassword("123");

        User salvo = userService.save(user);
        userService.delete(salvo.getId());

        assertThat(userService.findAll()).isEmpty();
        assertThat(userService.findById(salvo.getId())).isNull();
    }
}