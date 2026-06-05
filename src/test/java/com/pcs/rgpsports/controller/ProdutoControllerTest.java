package com.pcs.rgpsports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Produto;
import com.pcs.rgpsports.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
@AutoConfigureMockMvc(addFilters = false) // desativa os filtros de seguranca no teste
class ProdutoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private ProdutoService service;

    private Produto exemplo() {
        return Produto.builder()
                .id(1L).time("Flamengo").modalidade("Futebol").estoque(10)
                .tamanho("M").genero("Masculino").preco(new BigDecimal("199.90"))
                .status("ativo").build();
    }

    @Test
    void deveRetornar200AoListar() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(exemplo()));
        mvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time").value("Flamengo"));
    }

    @Test
    void deveRetornar200AoBuscarPorId() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo());
        mvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404QuandoNaoExistir() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Produto não encontrado com id: 99"));
        mvc.perform(get("/produtos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornar201AoCriar() throws Exception {
        when(service.salvar(any(Produto.class))).thenReturn(exemplo());
        Map<String, Object> body = Map.of(
                "time", "Flamengo",
                "modalidade", "Futebol",
                "estoque", 10,
                "tamanho", "M",
                "genero", "Masculino",
                "preco", new BigDecimal("199.90")
        );
        mvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.time").value("Flamengo"));
    }

    @Test
    void deveRetornar400QuandoBodyInvalido() throws Exception {
        Map<String, Object> body = Map.of(
                "time", "",            // invalido: viola @NotBlank
                "modalidade", "Futebol",
                "estoque", 10,
                "tamanho", "M",
                "genero", "Masculino",
                "preco", new BigDecimal("199.90")
        );
        mvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void deveRetornar204AoDeletar() throws Exception {
        mvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());
    }
}