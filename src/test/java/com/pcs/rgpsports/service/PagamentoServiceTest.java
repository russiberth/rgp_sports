package com.pcs.rgpsports.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Pagamento;
import com.pcs.rgpsports.repository.PagamentoRepository;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository repository;

    @InjectMocks
    private PagamentoService service;

    private Pagamento exemplo(String status) {
        return Pagamento.builder()
                .id(1L)
                .metodo("PIX")
                .statusTransacao(status)
                .valor(new BigDecimal("150.00"))
                .idTransacaoGateway("TX123")
                .parcelas(1)
                .build();
    }

    @Test
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(exemplo("pendente")));
        assertThat(service.listarTodos()).hasSize(1);
    }

    @Test
    void deveBuscarPorIdQuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(exemplo("pendente")));
        assertThat(service.buscarPorId(1L).getMetodo()).isEqualTo("PIX");
    }

    @Test
    void deveLancar404QuandoNaoEncontrar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveBuscarPorPedido() {
        when(repository.findByPedidoId(7L)).thenReturn(Optional.of(exemplo("pendente")));
        assertThat(service.buscarPorPedido(7L).getId()).isEqualTo(1L);
    }

    @Test
    void deveLancar404QuandoPedidoSemPagamento() {
        when(repository.findByPedidoId(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorPedido(99L));
    }

    @Test
    void deveBuscarPorTransacao() {
        when(repository.findByIdTransacaoGateway("TX123")).thenReturn(Optional.of(exemplo("pendente")));
        assertThat(service.buscarPorTransacao("TX123").getIdTransacaoGateway()).isEqualTo("TX123");
    }

    @Test
    void deveAtualizarStatus() {
        Pagamento p = exemplo("pendente");
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(repository.save(any(Pagamento.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.atualizarStatus(1L, "capturada").getStatusTransacao()).isEqualTo("capturada");
    }

    @Test
    void deveEstornarPagamentoCapturado() {
        Pagamento p = exemplo("capturada");
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(repository.save(any(Pagamento.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.estornar(1L).getStatusTransacao()).isEqualTo("estornada");
    }

    @Test
    void naoDeveEstornarPagamentoPendente() {
        Pagamento p = exemplo("pendente");
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        assertThrows(IllegalStateException.class, () -> service.estornar(1L));
        verify(repository, never()).save(any());
    }

    @Test
    void deveDeletarPagamento() {
        Pagamento p = exemplo("pendente");
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        service.deletar(1L);
        verify(repository).delete(p);
    }
}