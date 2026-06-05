package com.pcs.rgpsports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Avaliacao;
import com.pcs.rgpsports.model.Produto;
import com.pcs.rgpsports.model.Usuario;
import com.pcs.rgpsports.service.AvaliacaoService;
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

@WebMvcTest(AvaliacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AvaliacaoControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private AvaliacaoService service;

    private Avaliacao exemplo() {
        return Avaliacao.builder()
                .id(1L).nota(5).comentario("Excelente")
                .produto(Produto.builder().id(10L).build())
                .usuario(Usuario.builder().id(20L).build())
                .statusModeracao("aprovado")
                .build();
    }

    @Test
    void deveListar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(exemplo()));
        mvc.perform(get("/avaliacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nota").value(5));
    }

    @Test
    void deveBuscarPorId200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo());
        mvc.perform(get("/avaliacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Avaliação não encontrada com id: 99"));
        mvc.perform(get("/avaliacoes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveListarPorProduto200() throws Exception {
        when(service.listarPorProduto(10L)).thenReturn(List.of(exemplo()));
        mvc.perform(get("/avaliacoes/produto/10"))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarMedia200() throws Exception {
        when(service.mediaNotasPorProduto(10L)).thenReturn(4.5);
        mvc.perform(get("/avaliacoes/produto/10/media"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));
    }

    @Test
    void deveCriar201() throws Exception {
        when(service.salvar(any(Avaliacao.class))).thenReturn(exemplo());
        Map<String, Object> body = Map.of(
                "nota", 5,
                "comentario", "Excelente",
                "produto", Map.of("id", 10),
                "usuario", Map.of("id", 20)
        );
        mvc.perform(post("/avaliacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveDeletar204() throws Exception {
        mvc.perform(delete("/avaliacoes/1"))
                .andExpect(status().isNoContent());
    }
}