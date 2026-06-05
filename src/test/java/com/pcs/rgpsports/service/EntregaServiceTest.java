package com.pcs.rgpsports.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Entrega;
import com.pcs.rgpsports.repository.EntregaRepository;

@ExtendWith(MockitoExtension.class)
class EntregaServiceTest {

    @Mock
    private EntregaRepository repository;

    @InjectMocks
    private EntregaService service;

    private Entrega exemplo(String status) {
        return Entrega.builder()
                .id(1L)
                .codigoRastreio("BR123")
                .statusEntrega(status)
                .build();
    }

    @Test
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(exemplo("aguardando")));
        assertThat(service.listarTodos()).hasSize(1);
    }

    @Test
    void deveBuscarPorIdQuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(exemplo("aguardando")));
        assertThat(service.buscarPorId(1L).getId()).isEqualTo(1L);
    }

    @Test
    void deveLancar404PorId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveBuscarPorPedido() {
        when(repository.findByPedidoId(7L)).thenReturn(Optional.of(exemplo("aguardando")));
        assertThat(service.buscarPorPedido(7L).getId()).isEqualTo(1L);
    }

    @Test
    void deveLancar404PorPedido() {
        when(repository.findByPedidoId(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorPedido(99L));
    }

    @Test
    void deveBuscarPorRastreio() {
        when(repository.findByCodigoRastreio("BR123")).thenReturn(Optional.of(exemplo("enviado")));
        assertThat(service.buscarPorRastreio("BR123").getCodigoRastreio()).isEqualTo("BR123");
    }

    @Test
    void deveLancar404PorRastreio() {
        when(repository.findByCodigoRastreio("X")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorRastreio("X"));
    }

    @Test
    void deveAtualizarStatusParaEnviado() {
        Entrega e = exemplo("aguardando");
        when(repository.findById(1L)).thenReturn(Optional.of(e));
        when(repository.save(any(Entrega.class))).thenAnswer(i -> i.getArgument(0));
        Entrega r = service.atualizarStatus(1L, "enviado");
        assertThat(r.getStatusEntrega()).isEqualTo("enviado");
        assertThat(r.getDataEnvio()).isNotNull();
    }

    @Test
    void deveAtualizarStatusParaEntregue() {
        Entrega e = exemplo("enviado");
        when(repository.findById(1L)).thenReturn(Optional.of(e));
        when(repository.save(any(Entrega.class))).thenAnswer(i -> i.getArgument(0));
        Entrega r = service.atualizarStatus(1L, "entregue");
        assertThat(r.getStatusEntrega()).isEqualTo("entregue");
        assertThat(r.getDataEntrega()).isNotNull();
    }

    @Test
    void deveAdicionarRastreio() {
        Entrega e = exemplo("aguardando");
        when(repository.findById(1L)).thenReturn(Optional.of(e));
        when(repository.save(any(Entrega.class))).thenAnswer(i -> i.getArgument(0));
        Entrega r = service.adicionarRastreio(1L, "NOVO999");
        assertThat(r.getCodigoRastreio()).isEqualTo("NOVO999");
    }

    @Test
    void deveDeletar() {
        Entrega e = exemplo("aguardando");
        when(repository.findById(1L)).thenReturn(Optional.of(e));
        service.deletar(1L);
        verify(repository).delete(e);
    }
}