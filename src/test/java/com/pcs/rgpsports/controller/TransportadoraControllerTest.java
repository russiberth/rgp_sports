package com.pcs.rgpsports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Transportadora;
import com.pcs.rgpsports.service.TransportadoraService;
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

@WebMvcTest(TransportadoraController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransportadoraControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private TransportadoraService service;

    private Transportadora exemplo() {
        return Transportadora.builder()
                .id(1L).nome("Correios").cnpj("12345678000199")
                .telefone("11999999999").email("contato@correios.com")
                .build();
    }

    @Test
    void deveListar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(exemplo()));
        mvc.perform(get("/transportadoras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Correios"));
    }

    @Test
    void deveBuscarPorId200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo());
        mvc.perform(get("/transportadoras/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Transportadora não encontrada com id: 99"));
        mvc.perform(get("/transportadoras/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveCriar201() throws Exception {
        when(service.salvar(any(Transportadora.class))).thenReturn(exemplo());
        Map<String, Object> body = Map.of(
                "nome", "Correios",
                "cnpj", "12345678000199"
        );
        mvc.perform(post("/transportadoras").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Correios"));
    }

    @Test
    void deveRetornar400QuandoInvalido() throws Exception {
        Map<String, Object> body = Map.of(
                "nome", "",   // viola @NotBlank
                "cnpj", "12345678000199"
        );
        mvc.perform(post("/transportadoras").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizar200() throws Exception {
        when(service.atualizar(any(Long.class), any(Transportadora.class))).thenReturn(exemplo());
        Map<String, Object> body = Map.of(
                "nome", "Correios",
                "cnpj", "12345678000199"
        );
        mvc.perform(put("/transportadoras/1").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar204() throws Exception {
        mvc.perform(delete("/transportadoras/1"))
                .andExpect(status().isNoContent());
    }
}