package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.ItemCarrinho;
import com.pcs.rgpsports.service.ItemCarrinhoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemCarrinhoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemCarrinhoControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private ItemCarrinhoService service;

    private ItemCarrinho exemplo() {
        return ItemCarrinho.builder().id(1L).quantidade(3).build();
    }

    @Test
    void deveListarPorCarrinho200() throws Exception {
        when(service.listarPorCarrinho(5L)).thenReturn(List.of(exemplo()));
        mvc.perform(get("/itens-carrinho/carrinho/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deveBuscarPorId200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo());
        mvc.perform(get("/itens-carrinho/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Item não encontrado com id: 99"));
        mvc.perform(get("/itens-carrinho/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveAtualizarQuantidade200() throws Exception {
        when(service.atualizarQuantidade(eq(1L), any())).thenReturn(exemplo());
        mvc.perform(put("/itens-carrinho/1/quantidade").param("quantidade", "5"))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar204() throws Exception {
        mvc.perform(delete("/itens-carrinho/1"))
                .andExpect(status().isNoContent());
    }
}