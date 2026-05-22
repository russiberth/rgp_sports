package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Transportadora;
import com.pcs.rgpsports.repository.TransportadoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportadoraService {

    private final TransportadoraRepository transportadoraRepository;

    // Retorna todas as transportadoras
    @Transactional(readOnly = true)
    public List<Transportadora> listarTodos() {
        return transportadoraRepository.findAll();
    }

    // Busca transportadora por ID
    @Transactional(readOnly = true)
    public Transportadora buscarPorId(Long id) {
        return transportadoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora não encontrada com id: " + id));
    }

    // Salva nova transportadora — valida CNPJ duplicado
    @Transactional
    public Transportadora salvar(Transportadora transportadora) {
        if (transportadoraRepository.existsByCnpj(transportadora.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado: " + transportadora.getCnpj());
        }
        return transportadoraRepository.save(transportadora);
    }

    // Atualiza transportadora existente
    @Transactional
    public Transportadora atualizar(Long id, Transportadora transportadoraAtualizada) {
        Transportadora transportadora = buscarPorId(id);
        transportadora.setNome(transportadoraAtualizada.getNome());
        transportadora.setTelefone(transportadoraAtualizada.getTelefone());
        transportadora.setEmail(transportadoraAtualizada.getEmail());
        transportadora.setSiteApiRastreio(transportadoraAtualizada.getSiteApiRastreio());
        transportadora.setRegioesAtendidas(transportadoraAtualizada.getRegioesAtendidas());
        return transportadoraRepository.save(transportadora);
    }

    // Remove transportadora por ID
    @Transactional
    public void deletar(Long id) {
        Transportadora transportadora = buscarPorId(id);
        transportadoraRepository.delete(transportadora);
    }
}
