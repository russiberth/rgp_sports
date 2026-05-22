package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Produto;
import com.pcs.rgpsports.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    // Retorna todos os produtos
    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // Busca produto por ID — lança exceção se não encontrar
    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
    }

    // Salva novo produto no banco
    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    // Atualiza produto existente
    @Transactional
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id);
        produto.setTime(produtoAtualizado.getTime());
        produto.setModalidade(produtoAtualizado.getModalidade());
        produto.setEstoque(produtoAtualizado.getEstoque());
        produto.setTamanho(produtoAtualizado.getTamanho());
        produto.setGenero(produtoAtualizado.getGenero());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setTemporada(produtoAtualizado.getTemporada());
        produto.setStatus(produtoAtualizado.getStatus());
        return produtoRepository.save(produto);
    }

    // Remove produto por ID
    @Transactional
    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }

    // Busca produtos por modalidade
    @Transactional(readOnly = true)
    public List<Produto> buscarPorModalidade(String modalidade) {
        return produtoRepository.findByModalidade(modalidade);
    }

    // Busca produtos por nome do time
    @Transactional(readOnly = true)
    public List<Produto> buscarPorTime(String time) {
        return produtoRepository.findByTimeContainingIgnoreCase(time);
    }
}
