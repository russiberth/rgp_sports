package com.pcs.rgpsports.dto.cupom;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// O que o cliente ENVIA ao cadastrar um cupom
// Sem quantidadeUsada — controlada pelo servidor
public record CupomRequestDTO(

        @NotBlank(message = "Código do cupom é obrigatório")
        String codigo,

        @NotBlank(message = "Tipo é obrigatório")
        String tipo, // PERCENTUAL ou VALOR_FIXO

        @NotNull(message = "Valor do desconto é obrigatório")
        @DecimalMin(value = "0.01", message = "Desconto deve ser maior que zero")
        BigDecimal valorDesconto,

        @NotNull(message = "Validade é obrigatória")
        @Future(message = "Validade deve ser uma data futura")
        LocalDate validade,

        @NotNull(message = "Limite de uso é obrigatório")
        @Min(value = 1, message = "Limite de uso deve ser pelo menos 1")
        Integer limiteUso,

        BigDecimal valorMinimoPedido // opcional
) {}
