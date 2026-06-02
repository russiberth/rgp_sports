package com.pcs.rgpsports.dto.produto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

// O que o cliente ENVIA ao cadastrar ou atualizar um produto
// Sem id, sem status — esses são gerados pelo servidor
public record ProdutoRequestDTO(

        @NotBlank(message = "Time é obrigatório")
        String time,

        @NotBlank(message = "Modalidade é obrigatória")
        String modalidade,

        @NotNull(message = "Estoque é obrigatório")
        @Min(value = 0, message = "Estoque não pode ser negativo")
        Integer estoque,

        @NotBlank(message = "Tamanho é obrigatório")
        String tamanho,

        @NotBlank(message = "Gênero é obrigatório")
        String genero,

        List<String> imagens,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        BigDecimal preco,

        String descricao,

        String temporada
) {}
