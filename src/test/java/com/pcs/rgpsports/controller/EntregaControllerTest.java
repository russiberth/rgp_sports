package com.pcs.rgpsports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Entrega;
import com.pcs.rgpsports.service.EntregaService;
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

@WebMvcTest(EntregaController.class)
@AutoConfigureMockMvc(addFilters = false)
class EntregaControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private EntregaService service;

    private Entrega exemplo(String status) {
        return Entrega.builder()
                .id(1L).codigoRastreio("BR123").statusEntrega(status)
                .build();
    }

    @Test
    void deveListar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(exemplo("aguardando")));
        mvc.perform(get("/entregas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deveBuscarPorId200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo("aguardando"));
        mvc.perform(get("/entregas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoRastreio").value("BR123"));
    }

    @Test
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Entrega não encontrada com id: 99"));
        mvc.perform(get("/entregas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveBuscarPorPedido200() throws Exception {
        when(service.buscarPorPedido(7L)).thenReturn(exemplo("aguardando"));
        mvc.perform(get("/entregas/pedido/7"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorRastreio200() throws Exception {
        when(service.buscarPorRastreio("BR123")).thenReturn(exemplo("enviado"));
        mvc.perform(get("/entregas/rastreio/BR123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoRastreio").value("BR123"));
    }

    @Test
    void deveAtualizarStatus200() throws Exception {
        when(service.atualizarStatus(eq(1L), any())).thenReturn(exemplo("entregue"));
        mvc.perform(put("/entregas/1/status").param("status", "entregue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusEntrega").value("entregue"));
    }

    @Test
    void deveAdicionarRastreio200() throws Exception {
        when(service.adicionarRastreio(eq(1L), any())).thenReturn(exemplo("aguardando"));
        mvc.perform(put("/entregas/1/rastreio").param("codigo", "NOVO999"))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar204() throws Exception {
        mvc.perform(delete("/entregas/1"))
                .andExpect(status().isNoContent());
    }
}