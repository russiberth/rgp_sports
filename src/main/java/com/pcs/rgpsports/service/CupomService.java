package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Cupom;
import com.pcs.rgpsports.repository.CupomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CupomService {

    private final CupomRepository cupomRepository;

    // Retorna todos os cupons
    @Transactional(readOnly = true)
    public List<Cupom> listarTodos() {
        return cupomRepository.findAll();
    }

    // Busca cupom por ID
    @Transactional(readOnly = true)
    public Cupom buscarPorId(Long id) {
        return cupomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado com id: " + id));
    }

    // Busca cupom por código — valida se está ativo e dentro da validade
    @Transactional(readOnly = true)
    public Cupom validarCupom(String codigo) {
        Cupom cupom = cupomRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado: " + codigo));

        if (!cupom.getStatus().equals("ativo")) {
            throw new IllegalStateException("Cupom inativo: " + codigo);
        }
        if (cupom.getValidade().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Cupom expirado: " + codigo);
        }
        if (cupom.getQuantidadeUsada() >= cupom.getLimiteUso()) {
            throw new IllegalStateException("Cupom atingiu o limite de uso: " + codigo);
        }
        return cupom;
    }

    // Salva novo cupom
    @Transactional
    public Cupom salvar(Cupom cupom) {
        if (cupomRepository.existsByCodigo(cupom.getCodigo())) {
            throw new IllegalArgumentException("Código de cupom já existe: " + cupom.getCodigo());
        }
        return cupomRepository.save(cupom);
    }

    // Atualiza cupom existente
    @Transactional
    public Cupom atualizar(Long id, Cupom cupomAtualizado) {
        Cupom cupom = buscarPorId(id);
        cupom.setTipo(cupomAtualizado.getTipo());
        cupom.setValorDesconto(cupomAtualizado.getValorDesconto());
        cupom.setValidade(cupomAtualizado.getValidade());
        cupom.setLimiteUso(cupomAtualizado.getLimiteUso());
        cupom.setValorMinimoPedido(cupomAtualizado.getValorMinimoPedido());
        cupom.setStatus(cupomAtualizado.getStatus());
        return cupomRepository.save(cupom);
    }

    // Remove cupom por ID
    @Transactional
    public void deletar(Long id) {
        Cupom cupom = buscarPorId(id);
        cupomRepository.delete(cupom);
    }
}
