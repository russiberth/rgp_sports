package com.pcs.rgpsports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Cupom;
import com.pcs.rgpsports.service.CupomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CupomController.class)
@AutoConfigureMockMvc(addFilters = false)
class CupomControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private CupomService service;

    private Cupom exemplo() {
        return Cupom.builder()
                .id(1L).codigo("PROMO10").tipo("PERCENTUAL")
                .valorDesconto(new BigDecimal("10.00"))
                .validade(LocalDate.now().plusDays(10))
                .limiteUso(100).quantidadeUsada(0).status("ativo")
                .build();
    }

    @Test
    void deveListar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(exemplo()));
        mvc.perform(get("/cupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("PROMO10"));
    }

    @Test
    void deveBuscarPorId200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo());
        mvc.perform(get("/cupons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Cupom não encontrado com id: 99"));
        mvc.perform(get("/cupons/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveValidarCupom200() throws Exception {
        when(service.validarCupom("PROMO10")).thenReturn(exemplo());
        mvc.perform(get("/cupons/validar/PROMO10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("PROMO10"));
    }

    @Test
    void deveCriar201() throws Exception {
        when(service.salvar(any(Cupom.class))).thenReturn(exemplo());
        Map<String, Object> body = Map.of(
                "codigo", "PROMO10",
                "tipo", "PERCENTUAL",
                "valorDesconto", new BigDecimal("10.00"),
                "validade", LocalDate.now().plusDays(10).toString(),
                "limiteUso", 100
        );
        mvc.perform(post("/cupons").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornar400QuandoInvalido() throws Exception {
        Map<String, Object> body = Map.of(
                "codigo", "",   // viola @NotBlank
                "tipo", "PERCENTUAL",
                "valorDesconto", new BigDecimal("10.00"),
                "validade", LocalDate.now().plusDays(10).toString(),
                "limiteUso", 100
        );
        mvc.perform(post("/cupons").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveDeletar204() throws Exception {
        mvc.perform(delete("/cupons/1"))
                .andExpect(status().isNoContent());
    }
}