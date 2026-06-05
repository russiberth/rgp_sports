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
import com.pcs.rgpsports.model.Avaliacao;
import com.pcs.rgpsports.model.Produto;
import com.pcs.rgpsports.model.Usuario;
import com.pcs.rgpsports.repository.AvaliacaoRepository;

@ExtendWith(MockitoExtension.class)
class AvaliacaoServiceTest {

    @Mock
    private AvaliacaoRepository repository;

    @InjectMocks
    private AvaliacaoService service;

    private Avaliacao exemplo() {
        return Avaliacao.builder()
                .id(1L)
                .nota(5)
                .comentario("Excelente")
                .produto(Produto.builder().id(10L).build())
                .usuario(Usuario.builder().id(20L).build())
                .statusModeracao("aprovado")
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
        assertThat(service.buscarPorId(1L).getNota()).isEqualTo(5);
    }

    @Test
    void deveLancar404PorId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveListarPorProduto() {
        when(repository.findByProdutoId(10L)).thenReturn(List.of(exemplo()));
        assertThat(service.listarPorProduto(10L)).hasSize(1);
    }

    @Test
    void deveCalcularMedia() {
        when(repository.calcularMediaNotasPorProduto(10L)).thenReturn(4.5);
        assertThat(service.mediaNotasPorProduto(10L)).isEqualTo(4.5);
    }

    @Test
    void deveSalvarAvaliacaoNova() {
        Avaliacao a = exemplo();
        when(repository.existsByProdutoIdAndUsuarioId(10L, 20L)).thenReturn(false);
        when(repository.save(any(Avaliacao.class))).thenReturn(a);
        assertThat(service.salvar(a).getNota()).isEqualTo(5);
    }

    @Test
    void naoDeveSalvarSeUsuarioJaAvaliou() {
        Avaliacao a = exemplo();
        when(repository.existsByProdutoIdAndUsuarioId(10L, 20L)).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> service.salvar(a));
        verify(repository, never()).save(any());
    }

    @Test
    void deveAtualizarAvaliacao() {
        Avaliacao existente = exemplo();
        Avaliacao nova = Avaliacao.builder().nota(3).comentario("Ok").statusModeracao("aprovado").build();
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Avaliacao.class))).thenAnswer(i -> i.getArgument(0));
        Avaliacao r = service.atualizar(1L, nova);
        assertThat(r.getNota()).isEqualTo(3);
        assertThat(r.getComentario()).isEqualTo("Ok");
    }

    @Test
    void deveDeletar() {
        Avaliacao a = exemplo();
        when(repository.findById(1L)).thenReturn(Optional.of(a));
        service.deletar(1L);
        verify(repository).delete(a);
    }
}