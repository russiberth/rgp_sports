package com.pcs.rgpsports.dto.produto;

import com.pcs.rgpsports.model.Produto;
import java.math.BigDecimal;
import java.util.List;

// O que o cliente RECEBE — inclui id e status gerados pelo servidor
public record ProdutoResponseDTO(
        Long id,
        String time,
        String modalidade,
        Integer estoque,
        String tamanho,
        String genero,
        List<String> imagens,
        BigDecimal preco,
        String descricao,
        String temporada,
        String status
) {
        // Converte Entity → DTO
        public static ProdutoResponseDTO from(Produto p) {
                return new ProdutoResponseDTO(
                        p.getId(),
                        p.getTime(),
                        p.getModalidade(),
                        p.getEstoque(),
                        p.getTamanho(),
                        p.getGenero(),
                        p.getImagens(),
                        p.getPreco(),
                        p.getDescricao(),
                        p.getTemporada(),
                        p.getStatus()
                );
        }
}
