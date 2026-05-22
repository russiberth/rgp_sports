package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Entrega;
import com.pcs.rgpsports.repository.EntregaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntregaService {

    private final EntregaRepository entregaRepository;

    // Retorna todas as entregas
    @Transactional(readOnly = true)
    public List<Entrega> listarTodos() {
        return entregaRepository.findAll();
    }

    // Busca entrega por ID
    @Transactional(readOnly = true)
    public Entrega buscarPorId(Long id) {
        return entregaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega não encontrada com id: " + id));
    }

    // Busca entrega pelo pedido
    @Transactional(readOnly = true)
    public Entrega buscarPorPedido(Long pedidoId) {
        return entregaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega não encontrada para o pedido: " + pedidoId));
    }

    // Busca entrega pelo código de rastreio
    @Transactional(readOnly = true)
    public Entrega buscarPorRastreio(String codigo) {
        return entregaRepository.findByCodigoRastreio(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega não encontrada com rastreio: " + codigo));
    }

    // Registra nova entrega
    @Transactional
    public Entrega salvar(Entrega entrega) {
        return entregaRepository.save(entrega);
    }

    // Atualiza status da entrega
    @Transactional
    public Entrega atualizarStatus(Long id, String novoStatus) {
        Entrega entrega = buscarPorId(id);
        entrega.setStatusEntrega(novoStatus);

        // Se o status for "enviado", registra data de envio
        if (novoStatus.equals("enviado")) {
            entrega.setDataEnvio(LocalDateTime.now());
        }

        // Se o status for "entregue", registra data de entrega
        if (novoStatus.equals("entregue")) {
            entrega.setDataEntrega(LocalDateTime.now());
        }

        return entregaRepository.save(entrega);
    }

    // Adiciona código de rastreio à entrega
    @Transactional
    public Entrega adicionarRastreio(Long id, String codigoRastreio) {
        Entrega entrega = buscarPorId(id);
        entrega.setCodigoRastreio(codigoRastreio);
        return entregaRepository.save(entrega);
    }

    // Remove entrega por ID
    @Transactional
    public void deletar(Long id) {
        Entrega entrega = buscarPorId(id);
        entregaRepository.delete(entrega);
    }
}
