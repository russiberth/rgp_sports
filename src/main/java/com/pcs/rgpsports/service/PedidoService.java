package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Pedido;
import com.pcs.rgpsports.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    // Retorna todos os pedidos
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    // Busca pedido por ID — lança exceção se não encontrar
    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
    }

    // Retorna todos os pedidos de um usuário
    @Transactional(readOnly = true)
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    // Salva novo pedido
    @Transactional
    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    // Atualiza status do pedido
    @Transactional
    public Pedido atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    // Cancela pedido — só permite se status for pendente ou pago
    @Transactional
    public Pedido cancelar(Long id) {
        Pedido pedido = buscarPorId(id);
        if (!pedido.getStatus().equals("pendente") && !pedido.getStatus().equals("pago")) {
            throw new IllegalStateException("Pedido não pode ser cancelado. Status atual: " + pedido.getStatus());
        }
        pedido.setStatus("cancelado");
        return pedidoRepository.save(pedido);
    }

    // Remove pedido por ID
    @Transactional
    public void deletar(Long id) {
        Pedido pedido = buscarPorId(id);
        pedidoRepository.delete(pedido);
    }
}
