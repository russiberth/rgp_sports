package com.pcs.rgpsports.repository;

import com.pcs.rgpsports.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByProdutoId(Long produtoId);

    List<Avaliacao> findByUsuarioId(Long usuarioId);

    List<Avaliacao> findByStatusModeracao(String statusModeracao);

    boolean existsByProdutoIdAndUsuarioId(Long produtoId, Long usuarioId);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.produto.id = :produtoId")
    Double calcularMediaNotasPorProduto(Long produtoId);
}
