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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Transportadora;
import com.pcs.rgpsports.repository.TransportadoraRepository;

@ExtendWith(MockitoExtension.class)
class TransportadoraServiceTest {

    @Mock
    private TransportadoraRepository repository;

    @InjectMocks
    private TransportadoraService service;

    private Transportadora exemplo() {
        return Transportadora.builder()
                .id(1L)
                .nome("Correios")
                .cnpj("12345678000199")
                .telefone("11999999999")
                .email("contato@correios.com")
                .build();
    }

    @Test
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(exemplo()));
        assertThat(service.listarTodos()).hasSize(1);
    }

    @Test
    void deveBuscarPorIdQuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(exemplo()));
        assertThat(service.buscarPorId(1L).getNome()).isEqualTo("Correios");
    }

    @Test
    void deveLancar404QuandoNaoEncontrar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveSalvarNova() {
        Transportadora t = exemplo();
        when(repository.existsByCnpj("12345678000199")).thenReturn(false);
        when(repository.save(any(Transportadora.class))).thenReturn(t);
        assertThat(service.salvar(t).getNome()).isEqualTo("Correios");
    }

    @Test
    void naoDeveSalvarComCnpjDuplicado() {
        Transportadora t = exemplo();
        when(repository.existsByCnpj("12345678000199")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(t));
        verify(repository, never()).save(any());
    }

    @Test
    void deveAtualizar() {
        Transportadora existente = exemplo();
        Transportadora nova = Transportadora.builder().nome("Jadlog").telefone("11888888888").build();
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Transportadora.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.atualizar(1L, nova).getNome()).isEqualTo("Jadlog");
    }

    @Test
    void deveDeletar() {
        Transportadora t = exemplo();
        when(repository.findById(1L)).thenReturn(Optional.of(t));
        service.deletar(1L);
        verify(repository).delete(t);
    }
}