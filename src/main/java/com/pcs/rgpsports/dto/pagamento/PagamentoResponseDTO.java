package com.pcs.rgpsports.dto.pagamento;

import com.pcs.rgpsports.model.Pagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// O que o cliente RECEBE — sem idTransacaoGateway (dado interno)
public record PagamentoResponseDTO(
        Long id,
        Long pedidoId,
        String metodo,
        String statusTransacao,
        BigDecimal valor,
        Integer parcelas,
        LocalDateTime dataPagamento
) {
        // Converte Entity → DTO
        public static PagamentoResponseDTO from(Pagamento p) {
                return new PagamentoResponseDTO(
                        p.getId(),
                        p.getPedido().getId(),
                        p.getMetodo(),
                        p.getStatusTransacao(),
                        p.getValor(),
                        p.getParcelas(),
                        p.getDataPagamento()
                );
        }
}
