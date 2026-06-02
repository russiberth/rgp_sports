package com.pcs.rgpsports.dto.pedido;

import jakarta.validation.constraints.*;

// O que o cliente ENVIA ao criar um pedido
// Sem status, valorTotal, dataPedido — gerados pelo servidor
public record PedidoRequestDTO(

        @NotNull(message = "ID do usuário é obrigatório")
        Long usuarioId,

        @NotBlank(message = "Endereço de entrega é obrigatório")
        String enderecoEntrega,

        Long cupomId // opcional
) {}
