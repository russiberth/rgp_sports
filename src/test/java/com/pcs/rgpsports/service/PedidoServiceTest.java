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
import com.pcs.rgpsports.model.Pedido;
import com.pcs.rgpsports.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @InjectMocks
    private PedidoService service;

    private Pedido exemplo(String status) {
        return Pedido.builder()
                .id(1L)
                .subtotal(new BigDecimal("200.00"))
                .desconto(BigDecimal.ZERO)
                .valorTotal(new BigDecimal("200.00"))
                .status(status)
                .build();
    }

    @Test
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(exemplo("pendente")));
        assertThat(service.listarTodos()).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void deveBuscarPorIdQuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(exemplo("pendente")));
        Pedido p = service.buscarPorId(1L);
        assertThat(p.getId()).isEqualTo(1L);
    }

    @Test
    void deveLancar404QuandoNaoEncontrar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveListarPorUsuario() {
        when(repository.findByUsuarioId(5L)).thenReturn(List.of(exemplo("pago")));
        assertThat(service.listarPorUsuario(5L)).hasSize(1);
        verify(repository).findByUsuarioId(5L);
    }

    @Test
    void deveSalvarPedido() {
        Pedido p = exemplo("pendente");
        when(repository.save(any(Pedido.class))).thenReturn(p);
        assertThat(service.salvar(p).getStatus()).isEqualTo("pendente");
    }

    @Test
    void deveAtualizarStatus() {
        Pedido p = exemplo("pendente");
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(repository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));
        Pedido atualizado = service.atualizarStatus(1L, "enviado");
        assertThat(atualizado.getStatus()).isEqualTo("enviado");
    }

    @Test
    void deveCancelarPedidoPendente() {
        Pedido p = exemplo("pendente");
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(repository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));
        Pedido cancelado = service.cancelar(1L);
        assertThat(cancelado.getStatus()).isEqualTo("cancelado");
    }

    @Test
    void naoDeveCancelarPedidoEnviado() {
        Pedido p = exemplo("enviado");
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        assertThrows(IllegalStateException.class, () -> service.cancelar(1L));
        verify(repository, never()).save(any());
    }

    @Test
    void deveDeletarPedido() {
        Pedido p = exemplo("pendente");
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        service.deletar(1L);
        verify(repository).delete(p);
    }
}