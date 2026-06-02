package com.pcs.rgpsports.dto.pedido;

import com.pcs.rgpsports.model.Pedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// O que o cliente RECEBE — dados completos do pedido
public record PedidoResponseDTO(
        Long id,
        String nomeUsuario,
        String enderecoEntrega,
        BigDecimal subtotal,
        BigDecimal desconto,
        BigDecimal valorTotal,
        String status,
        LocalDateTime dataPedido,
        String cupomAplicado
) {
        // Converte Entity → DTO
        public static PedidoResponseDTO from(Pedido p) {
                return new PedidoResponseDTO(
                        p.getId(),
                        p.getUsuario().getNomeCompleto(),
                        p.getEnderecoEntrega(),
                        p.getSubtotal(),
                        p.getDesconto(),
                        p.getValorTotal(),
                        p.getStatus(),
                        p.getDataPedido(),
                        p.getCupom() != null ? p.getCupom().getCodigo() : null
                );
        }
}
