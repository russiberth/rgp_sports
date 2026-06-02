package com.pcs.rgpsports.dto.cupom;

import com.pcs.rgpsports.model.Cupom;
import java.math.BigDecimal;
import java.time.LocalDate;

// O que o cliente RECEBE — inclui quantidadeUsada e status
public record CupomResponseDTO(
        Long id,
        String codigo,
        String tipo,
        BigDecimal valorDesconto,
        LocalDate validade,
        Integer limiteUso,
        Integer quantidadeUsada,
        BigDecimal valorMinimoPedido,
        String status
) {
        // Converte Entity → DTO
        public static CupomResponseDTO from(Cupom c) {
                return new CupomResponseDTO(
                        c.getId(),
                        c.getCodigo(),
                        c.getTipo(),
                        c.getValorDesconto(),
                        c.getValidade(),
                        c.getLimiteUso(),
                        c.getQuantidadeUsada(),
                        c.getValorMinimoPedido(),
                        c.getStatus()
                );
        }
}
