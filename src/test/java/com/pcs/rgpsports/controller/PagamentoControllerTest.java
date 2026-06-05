package com.pcs.rgpsports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Pagamento;
import com.pcs.rgpsports.model.Pedido;
import com.pcs.rgpsports.service.PagamentoService;
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

@WebMvcTest(PagamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PagamentoControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private PagamentoService service;

    private Pagamento exemplo(String status) {
        Pedido pedido = Pedido.builder().id(7L).build();
        return Pagamento.builder()
                .id(1L).pedido(pedido).metodo("PIX").statusTransacao(status)
                .valor(new BigDecimal("150.00")).parcelas(1)
                .build();
    }

    @Test
    void deveListar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(exemplo("pendente")));
        mvc.perform(get("/pagamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].metodo").value("PIX"));
    }

    @Test
    void deveBuscarPorId200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo("pendente"));
        mvc.perform(get("/pagamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Pagamento não encontrado com id: 99"));
        mvc.perform(get("/pagamentos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveBuscarPorPedido200() throws Exception {
        when(service.buscarPorPedido(7L)).thenReturn(exemplo("pendente"));
        mvc.perform(get("/pagamentos/pedido/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(7));
    }

    @Test
    void deveCriar201() throws Exception {
        when(service.salvar(any(Pagamento.class))).thenReturn(exemplo("pendente"));
        Map<String, Object> body = Map.of(
                "pedidoId", 7,
                "metodo", "PIX",
                "valor", new BigDecimal("150.00")
        );
        mvc.perform(post("/pagamentos").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornar400QuandoInvalido() throws Exception {
        Map<String, Object> body = Map.of(
                "metodo", "PIX",          // falta pedidoId e valor (viola @NotNull)
                "parcelas", 1
        );
        mvc.perform(post("/pagamentos").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveEstornar200() throws Exception {
        when(service.estornar(1L)).thenReturn(exemplo("estornada"));
        mvc.perform(put("/pagamentos/1/estornar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusTransacao").value("estornada"));
    }

    @Test
    void deveDeletar204() throws Exception {
        mvc.perform(delete("/pagamentos/1"))
                .andExpect(status().isNoContent());
    }
}