package com.pcs.rgpsports.dto.pagamento;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

// O que o cliente ENVIA ao registrar um pagamento
// Sem idTransacaoGateway — gerado pelo gateway externo
public record PagamentoRequestDTO(

        @NotNull(message = "ID do pedido é obrigatório")
        Long pedidoId,

        @NotBlank(message = "Método de pagamento é obrigatório")
        String metodo, // PIX, CARTAO, BOLETO

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valor,

        Integer parcelas // opcional — só para cartão
) {}
