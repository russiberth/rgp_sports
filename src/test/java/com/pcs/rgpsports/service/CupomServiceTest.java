package com.pcs.rgpsports.service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.pcs.rgpsports.model.Cupom;
import com.pcs.rgpsports.repository.CupomRepository;

@ExtendWith(MockitoExtension.class)
class CupomServiceTest {

    @Mock
    private CupomRepository repository;

    @InjectMocks
    private CupomService service;

    private Cupom exemplo(String status, LocalDate validade, int usada, int limite) {
        return Cupom.builder()
                .id(1L)
                .codigo("PROMO10")
                .tipo("PERCENTUAL")
                .valorDesconto(new BigDecimal("10.00"))
                .validade(validade)
                .limiteUso(limite)
                .quantidadeUsada(usada)
                .status(status)
                .build();
    }

    @Test
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(exemplo("ativo", LocalDate.now().plusDays(5), 0, 10)));
        assertThat(service.listarTodos()).hasSize(1);
    }

    @Test
    void deveBuscarPorIdQuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(exemplo("ativo", LocalDate.now().plusDays(5), 0, 10)));
        assertThat(service.buscarPorId(1L).getCodigo()).isEqualTo("PROMO10");
    }

    @Test
    void deveLancar404QuandoNaoEncontrar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveValidarCupomAtivo() {
        when(repository.findByCodigo("PROMO10"))
                .thenReturn(Optional.of(exemplo("ativo", LocalDate.now().plusDays(5), 0, 10)));
        Cupom c = service.validarCupom("PROMO10");
        assertThat(c.getStatus()).isEqualTo("ativo");
    }

    @Test
    void naoDeveValidarCupomInativo() {
        when(repository.findByCodigo("PROMO10"))
                .thenReturn(Optional.of(exemplo("inativo", LocalDate.now().plusDays(5), 0, 10)));
        assertThrows(IllegalStateException.class, () -> service.validarCupom("PROMO10"));
    }

    @Test
    void naoDeveValidarCupomExpirado() {
        when(repository.findByCodigo("PROMO10"))
                .thenReturn(Optional.of(exemplo("ativo", LocalDate.now().minusDays(1), 0, 10)));
        assertThrows(IllegalStateException.class, () -> service.validarCupom("PROMO10"));
    }

    @Test
    void naoDeveValidarCupomNoLimite() {
        when(repository.findByCodigo("PROMO10"))
                .thenReturn(Optional.of(exemplo("ativo", LocalDate.now().plusDays(5), 10, 10)));
        assertThrows(IllegalStateException.class, () -> service.validarCupom("PROMO10"));
    }

    @Test
    void deveSalvarCupomNovo() {
        Cupom c = exemplo("ativo", LocalDate.now().plusDays(5), 0, 10);
        when(repository.existsByCodigo("PROMO10")).thenReturn(false);
        when(repository.save(any(Cupom.class))).thenReturn(c);
        assertThat(service.salvar(c).getCodigo()).isEqualTo("PROMO10");
    }

    @Test
    void naoDeveSalvarCupomComCodigoDuplicado() {
        Cupom c = exemplo("ativo", LocalDate.now().plusDays(5), 0, 10);
        when(repository.existsByCodigo("PROMO10")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(c));
        verify(repository, never()).save(any());
    }

    @Test
    void deveDeletarCupom() {
        Cupom c = exemplo("ativo", LocalDate.now().plusDays(5), 0, 10);
        when(repository.findById(1L)).thenReturn(Optional.of(c));
        service.deletar(1L);
        verify(repository).delete(c);
    }
}