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
import com.pcs.rgpsports.model.Produto;
import com.pcs.rgpsports.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    private Produto exemplo() {
        return Produto.builder()
                .id(1L)
                .time("Flamengo")
                .modalidade("Futebol")
                .estoque(10)
                .tamanho("M")
                .genero("Masculino")
                .preco(new BigDecimal("199.90"))
                .status("ativo")
                .build();
    }

    @Test
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(exemplo()));
        List<Produto> r = service.listarTodos();
        assertThat(r).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void deveBuscarPorIdQuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(exemplo()));
        Produto p = service.buscarPorId(1L);
        assertThat(p.getTime()).isEqualTo("Flamengo");
    }

    @Test
    void deveLancar404QuandoNaoEncontrar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
        verify(repository).findById(99L);
    }

    @Test
    void deveSalvarProduto() {
        Produto p = exemplo();
        when(repository.save(any(Produto.class))).thenReturn(p);
        Produto salvo = service.salvar(p);
        assertThat(salvo.getId()).isEqualTo(1L);
        verify(repository).save(p);
    }

    @Test
    void deveDeletarProduto() {
        Produto p = exemplo();
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        service.deletar(1L);
        verify(repository).delete(p);
    }

    @Test
    void deveLancar404AoDeletarInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deletar(99L));
        verify(repository, never()).delete(any());
    }
}