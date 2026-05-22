package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Avaliacao;
import com.pcs.rgpsports.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    // Retorna todas as avaliações
    @Transactional(readOnly = true)
    public List<Avaliacao> listarTodos() {
        return avaliacaoRepository.findAll();
    }

    // Busca avaliação por ID
    @Transactional(readOnly = true)
    public Avaliacao buscarPorId(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + id));
    }

    // Retorna todas as avaliações de um produto
    @Transactional(readOnly = true)
    public List<Avaliacao> listarPorProduto(Long produtoId) {
        return avaliacaoRepository.findByProdutoId(produtoId);
    }

    // Retorna média de notas de um produto
    @Transactional(readOnly = true)
    public Double mediaNotasPorProduto(Long produtoId) {
        return avaliacaoRepository.calcularMediaNotasPorProduto(produtoId);
    }

    // Salva nova avaliação — valida se usuário já avaliou o produto
    @Transactional
    public Avaliacao salvar(Avaliacao avaliacao) {
        boolean jaAvaliou = avaliacaoRepository.existsByProdutoIdAndUsuarioId(
                avaliacao.getProduto().getId(),
                avaliacao.getUsuario().getId()
        );
        if (jaAvaliou) {
            throw new IllegalStateException("Usuário já avaliou este produto.");
        }
        return avaliacaoRepository.save(avaliacao);
    }

    // Atualiza avaliação existente
    @Transactional
    public Avaliacao atualizar(Long id, Avaliacao avaliacaoAtualizada) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacao.setNota(avaliacaoAtualizada.getNota());
        avaliacao.setComentario(avaliacaoAtualizada.getComentario());
        avaliacao.setStatusModeracao(avaliacaoAtualizada.getStatusModeracao());
        return avaliacaoRepository.save(avaliacao);
    }

    // Remove avaliação por ID
    @Transactional
    public void deletar(Long id) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacaoRepository.delete(avaliacao);
    }
}
