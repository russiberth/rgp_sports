package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.ItemPedido;
import com.pcs.rgpsports.repository.ItemPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    // Retorna todos os itens de um pedido
    @Transactional(readOnly = true)
    public List<ItemPedido> listarPorPedido(Long pedidoId) {
        return itemPedidoRepository.findByPedidoId(pedidoId);
    }

    // Busca item por ID
    @Transactional(readOnly = true)
    public ItemPedido buscarPorId(Long id) {
        return itemPedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do pedido não encontrado com id: " + id));
    }

    // Salva novo item de pedido
    @Transactional
    public ItemPedido salvar(ItemPedido item) {
        return itemPedidoRepository.save(item);
    }

    // Atualiza quantidade do item
    @Transactional
    public ItemPedido atualizarQuantidade(Long id, Integer novaQuantidade) {
        ItemPedido item = buscarPorId(id);
        item.setQuantidade(novaQuantidade);
        return itemPedidoRepository.save(item);
    }

    // Remove item do pedido
    @Transactional
    public void deletar(Long id) {
        ItemPedido item = buscarPorId(id);
        itemPedidoRepository.delete(item);
    }
}
