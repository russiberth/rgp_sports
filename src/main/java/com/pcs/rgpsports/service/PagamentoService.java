package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Pagamento;
import com.pcs.rgpsports.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    // Retorna todos os pagamentos
    @Transactional(readOnly = true)
    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    // Busca pagamento por ID
    @Transactional(readOnly = true)
    public Pagamento buscarPorId(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado com id: " + id));
    }

    // Busca pagamento pelo ID do pedido
    @Transactional(readOnly = true)
    public Pagamento buscarPorPedido(Long pedidoId) {
        return pagamentoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado para o pedido: " + pedidoId));
    }

    // Busca pagamento pelo ID da transação no gateway
    @Transactional(readOnly = true)
    public Pagamento buscarPorTransacao(String idTransacao) {
        return pagamentoRepository.findByIdTransacaoGateway(idTransacao)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada: " + idTransacao));
    }

    // Registra novo pagamento
    @Transactional
    public Pagamento salvar(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    // Atualiza status da transação — ex: de pendente para capturada
    @Transactional
    public Pagamento atualizarStatus(Long id, String novoStatus) {
        Pagamento pagamento = buscarPorId(id);
        pagamento.setStatusTransacao(novoStatus);
        return pagamentoRepository.save(pagamento);
    }

    // Estorna pagamento — muda status para estornada
    @Transactional
    public Pagamento estornar(Long id) {
        Pagamento pagamento = buscarPorId(id);
        if (!pagamento.getStatusTransacao().equals("capturada")) {
            throw new IllegalStateException("Só é possível estornar pagamentos capturados. Status atual: "
                    + pagamento.getStatusTransacao());
        }
        pagamento.setStatusTransacao("estornada");
        return pagamentoRepository.save(pagamento);
    }

    // Remove pagamento por ID
    @Transactional
    public void deletar(Long id) {
        Pagamento pagamento = buscarPorId(id);
        pagamentoRepository.delete(pagamento);
    }
}
