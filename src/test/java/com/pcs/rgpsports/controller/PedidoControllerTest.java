package com.pcs.rgpsports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Pedido;
import com.pcs.rgpsports.model.Usuario;
import com.pcs.rgpsports.service.PedidoService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PedidoControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private PedidoService service;

    private Pedido exemplo(String status) {
        Usuario u = Usuario.builder().id(5L).build();
        return Pedido.builder()
                .id(1L).usuario(u)
                .subtotal(new BigDecimal("200.00"))
                .desconto(BigDecimal.ZERO)
                .valorTotal(new BigDecimal("200.00"))
                .status(status)
                .build();
    }

    @Test
    void deveListar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(exemplo("pendente")));
        mvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deveBuscarPorId200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo("pendente"));
        mvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Pedido não encontrado com id: 99"));
        mvc.perform(get("/pedidos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveListarPorUsuario200() throws Exception {
        when(service.listarPorUsuario(5L)).thenReturn(List.of(exemplo("pago")));
        mvc.perform(get("/pedidos/usuario/5"))
                .andExpect(status().isOk());
    }

    @Test
    void deveCriar201() throws Exception {
        when(service.salvar(any(Pedido.class))).thenReturn(exemplo("pendente"));
        Map<String, Object> body = Map.of(
                "usuarioId", 5,
                "enderecoEntrega", "Rua A, 123"
        );
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveAtualizarStatus200() throws Exception {
        when(service.atualizarStatus(eq(1L), any())).thenReturn(exemplo("enviado"));
        mvc.perform(put("/pedidos/1/status").param("status", "enviado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("enviado"));
    }

    @Test
    void deveCancelar200() throws Exception {
        when(service.cancelar(1L)).thenReturn(exemplo("cancelado"));
        mvc.perform(put("/pedidos/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("cancelado"));
    }

    @Test
    void deveDeletar204() throws Exception {
        mvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());
    }
}