package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.ItemPedido;
import com.pcs.rgpsports.service.ItemPedidoService;
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

@WebMvcTest(ItemPedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemPedidoControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private ItemPedidoService service;

    private ItemPedido exemplo() {
        return ItemPedido.builder().id(1L).quantidade(2).build();
    }

    @Test
    void deveListarPorPedido200() throws Exception {
        when(service.listarPorPedido(7L)).thenReturn(List.of(exemplo()));
        mvc.perform(get("/itens-pedido/pedido/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deveBuscarPorId200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo());
        mvc.perform(get("/itens-pedido/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Item não encontrado com id: 99"));
        mvc.perform(get("/itens-pedido/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveAtualizarQuantidade200() throws Exception {
        when(service.atualizarQuantidade(eq(1L), any())).thenReturn(exemplo());
        mvc.perform(put("/itens-pedido/1/quantidade").param("quantidade", "5"))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar204() throws Exception {
        mvc.perform(delete("/itens-pedido/1"))
                .andExpect(status().isNoContent());
    }
}