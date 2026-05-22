package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.ItemCarrinho;
import com.pcs.rgpsports.repository.ItemCarrinhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCarrinhoService {

    private final ItemCarrinhoRepository itemCarrinhoRepository;

    // Retorna todos os itens de um carrinho
    @Transactional(readOnly = true)
    public List<ItemCarrinho> listarPorCarrinho(Long carrinhoId) {
        return itemCarrinhoRepository.findByCarrinhoId(carrinhoId);
    }

    // Busca item por ID
    @Transactional(readOnly = true)
    public ItemCarrinho buscarPorId(Long id) {
        return itemCarrinhoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do carrinho não encontrado com id: " + id));
    }

    // Salva novo item no carrinho
    @Transactional
    public ItemCarrinho salvar(ItemCarrinho item) {
        return itemCarrinhoRepository.save(item);
    }

    // Atualiza quantidade do item no carrinho
    @Transactional
    public ItemCarrinho atualizarQuantidade(Long id, Integer novaQuantidade) {
        ItemCarrinho item = buscarPorId(id);
        item.setQuantidade(novaQuantidade);
        return itemCarrinhoRepository.save(item);
    }

    // Remove item do carrinho
    @Transactional
    public void deletar(Long id) {
        ItemCarrinho item = buscarPorId(id);
        itemCarrinhoRepository.delete(item);
    }
}
