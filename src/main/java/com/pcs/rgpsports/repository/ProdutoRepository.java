package com.pcs.rgpsports.repository;

import com.pcs.rgpsports.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByStatus(String status);

    List<Produto> findByModalidade(String modalidade);

    List<Produto> findByTimeContainingIgnoreCase(String time);

    List<Produto> findByModalidadeAndStatus(String modalidade, String status);

    List<Produto> findByGeneroAndStatus(String genero, String status);

    List<Produto> findByTamanhoAndStatus(String tamanho, String status);

    List<Produto> findByEstoqueGreaterThan(Integer estoque);

    @Query("SELECT p FROM Produto p WHERE p.estoque = 0")
    List<Produto> findProdutosSemEstoque();
}
