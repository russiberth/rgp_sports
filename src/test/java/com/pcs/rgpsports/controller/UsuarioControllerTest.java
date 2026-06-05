package com.pcs.rgpsports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Usuario;
import com.pcs.rgpsports.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private UsuarioService service;

    private Usuario exemplo() {
        return Usuario.builder()
                .id(1L).nomeCompleto("João Silva").cpf("12345678900")
                .email("joao@email.com").senha("senha12345").status("ativo")
                .build();
    }

    @Test
    void deveListar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(exemplo()));
        mvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("joao@email.com"));
    }

    @Test
    void deveBuscarPorId200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo());
        mvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Usuário não encontrado com id: 99"));
        mvc.perform(get("/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveBuscarPorEmail200() throws Exception {
        when(service.buscarPorEmail("joao@email.com")).thenReturn(exemplo());
        mvc.perform(get("/usuarios/email/joao@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("João Silva"));
    }

    @Test
    void deveCriar201() throws Exception {
        when(service.salvar(any(Usuario.class))).thenReturn(exemplo());
        Map<String, Object> body = Map.of(
                "nomeCompleto", "João Silva",
                "cpf", "12345678900",
                "email", "joao@email.com",
                "senha", "senha12345"
        );
        mvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void deveRetornar400QuandoInvalido() throws Exception {
        Map<String, Object> body = Map.of(
                "nomeCompleto", "",      // viola @NotBlank
                "cpf", "12345678900",
                "email", "joao@email.com",
                "senha", "senha12345"
        );
        mvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveDeletar204() throws Exception {
        mvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}