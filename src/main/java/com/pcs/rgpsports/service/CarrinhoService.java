package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Carrinho;
import com.pcs.rgpsports.model.ItemCarrinho;
import com.pcs.rgpsports.model.Produto;
import com.pcs.rgpsports.repository.CarrinhoRepository;
import com.pcs.rgpsports.repository.ItemCarrinhoRepository;
import com.pcs.rgpsports.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final ProdutoRepository produtoRepository;

    // Busca carrinho do usuário — lança exceção se não encontrar
    @Transactional(readOnly = true)
    public Carrinho buscarPorUsuario(Long usuarioId) {
        return carrinhoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para o usuário: " + usuarioId));
    }

    // Adiciona produto ao carrinho — se já existir, incrementa quantidade
    @Transactional
    public Carrinho adicionarItem(Long usuarioId, Long produtoId, Integer quantidade) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para o usuário: " + usuarioId));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + produtoId));

        // Verifica se produto já está no carrinho
        itemCarrinhoRepository.findByCarrinhoIdAndProdutoId(carrinho.getId(), produtoId)
                .ifPresentOrElse(
                        // Se já existe, incrementa quantidade
                        item -> {
                            item.setQuantidade(item.getQuantidade() + quantidade);
                            itemCarrinhoRepository.save(item);
                        },
                        // Se não existe, cria novo item
                        () -> {
                            ItemCarrinho novoItem = ItemCarrinho.builder()
                                    .carrinho(carrinho)
                                    .produto(produto)
                                    .quantidade(quantidade)
                                    .build();
                            itemCarrinhoRepository.save(novoItem);
                        }
                );

        carrinho.setDataUltimaModificacao(LocalDateTime.now());
        return carrinhoRepository.save(carrinho);
    }

    // Remove item específico do carrinho
    @Transactional
    public void removerItem(Long usuarioId, Long produtoId) {
        Carrinho carrinho = buscarPorUsuario(usuarioId);
        ItemCarrinho item = itemCarrinhoRepository
                .findByCarrinhoIdAndProdutoId(carrinho.getId(), produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));
        itemCarrinhoRepository.delete(item);
        carrinho.setDataUltimaModificacao(LocalDateTime.now());
        carrinhoRepository.save(carrinho);
    }

    // Limpa todos os itens do carrinho
    @Transactional
    public void limparCarrinho(Long usuarioId) {
        Carrinho carrinho = buscarPorUsuario(usuarioId);
        itemCarrinhoRepository.deleteByCarrinhoId(carrinho.getId());
        carrinho.setDataUltimaModificacao(LocalDateTime.now());
        carrinhoRepository.save(carrinho);
    }
}
