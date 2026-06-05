package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Carrinho;
import com.pcs.rgpsports.service.CarrinhoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarrinhoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarrinhoControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private CarrinhoService service;

    private Carrinho exemplo() {
        return Carrinho.builder().id(1L).build();
    }

    @Test
    void deveBuscarPorUsuario200() throws Exception {
        when(service.buscarPorUsuario(5L)).thenReturn(exemplo());
        mvc.perform(get("/carrinho/usuario/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404QuandoSemCarrinho() throws Exception {
        when(service.buscarPorUsuario(99L))
                .thenThrow(new ResourceNotFoundException("Carrinho não encontrado para o usuário: 99"));
        mvc.perform(get("/carrinho/usuario/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveAdicionarItem200() throws Exception {
        when(service.adicionarItem(eq(5L), eq(10L), anyInt())).thenReturn(exemplo());
        mvc.perform(post("/carrinho/usuario/5/produto/10").param("quantidade", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRemoverItem204() throws Exception {
        mvc.perform(delete("/carrinho/usuario/5/produto/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveLimparCarrinho204() throws Exception {
        mvc.perform(delete("/carrinho/usuario/5/limpar"))
                .andExpect(status().isNoContent());
    }
}