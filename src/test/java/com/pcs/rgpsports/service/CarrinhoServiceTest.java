package com.pcs.rgpsports.service;

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
import com.pcs.rgpsports.model.Carrinho;
import com.pcs.rgpsports.model.ItemCarrinho;
import com.pcs.rgpsports.model.Produto;
import com.pcs.rgpsports.repository.CarrinhoRepository;
import com.pcs.rgpsports.repository.ItemCarrinhoRepository;
import com.pcs.rgpsports.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private ItemCarrinhoRepository itemCarrinhoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private CarrinhoService service;

    private Carrinho carrinhoExemplo() {
        return Carrinho.builder().id(1L).build();
    }

    private Produto produtoExemplo() {
        return Produto.builder().id(10L).time("Flamengo").build();
    }

    @Test
    void deveBuscarPorUsuarioQuandoExiste() {
        when(carrinhoRepository.findByUsuarioId(5L)).thenReturn(Optional.of(carrinhoExemplo()));
        Carrinho c = service.buscarPorUsuario(5L);
        assertThat(c.getId()).isEqualTo(1L);
    }

    @Test
    void deveLancar404QuandoCarrinhoNaoExiste() {
        when(carrinhoRepository.findByUsuarioId(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorUsuario(99L));
    }

    @Test
    void deveAdicionarItemNovoNoCarrinho() {
        when(carrinhoRepository.findByUsuarioId(5L)).thenReturn(Optional.of(carrinhoExemplo()));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produtoExemplo()));
        when(itemCarrinhoRepository.findByCarrinhoIdAndProdutoId(1L, 10L)).thenReturn(Optional.empty());
        when(carrinhoRepository.save(any(Carrinho.class))).thenAnswer(i -> i.getArgument(0));

        service.adicionarItem(5L, 10L, 2);

        verify(itemCarrinhoRepository).save(any(ItemCarrinho.class));
        verify(carrinhoRepository).save(any(Carrinho.class));
    }

    @Test
    void deveIncrementarQuantidadeSeItemJaExiste() {
        ItemCarrinho existente = ItemCarrinho.builder()
                .id(1L).carrinho(carrinhoExemplo()).produto(produtoExemplo()).quantidade(1).build();

        when(carrinhoRepository.findByUsuarioId(5L)).thenReturn(Optional.of(carrinhoExemplo()));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produtoExemplo()));
        when(itemCarrinhoRepository.findByCarrinhoIdAndProdutoId(1L, 10L)).thenReturn(Optional.of(existente));
        when(carrinhoRepository.save(any(Carrinho.class))).thenAnswer(i -> i.getArgument(0));

        service.adicionarItem(5L, 10L, 3);

        assertThat(existente.getQuantidade()).isEqualTo(4); // 1 + 3
        verify(itemCarrinhoRepository).save(existente);
    }

    @Test
    void deveRemoverItem() {
        ItemCarrinho item = ItemCarrinho.builder().id(1L).build();
        when(carrinhoRepository.findByUsuarioId(5L)).thenReturn(Optional.of(carrinhoExemplo()));
        when(itemCarrinhoRepository.findByCarrinhoIdAndProdutoId(1L, 10L)).thenReturn(Optional.of(item));
        when(carrinhoRepository.save(any(Carrinho.class))).thenAnswer(i -> i.getArgument(0));

        service.removerItem(5L, 10L);

        verify(itemCarrinhoRepository).delete(item);
    }

    @Test
    void deveLancar404AoRemoverItemInexistente() {
        when(carrinhoRepository.findByUsuarioId(5L)).thenReturn(Optional.of(carrinhoExemplo()));
        when(itemCarrinhoRepository.findByCarrinhoIdAndProdutoId(1L, 10L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.removerItem(5L, 10L));
    }

    @Test
    void deveLimparCarrinho() {
        when(carrinhoRepository.findByUsuarioId(5L)).thenReturn(Optional.of(carrinhoExemplo()));
        when(carrinhoRepository.save(any(Carrinho.class))).thenAnswer(i -> i.getArgument(0));

        service.limparCarrinho(5L);

        verify(itemCarrinhoRepository).deleteByCarrinhoId(1L);
    }
}