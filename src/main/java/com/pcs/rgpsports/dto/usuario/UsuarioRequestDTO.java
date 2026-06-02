package com.pcs.rgpsports.dto.usuario;

import jakarta.validation.constraints.*;

// O que o cliente ENVIA ao cadastrar usuário
// Inclui senha — mas ela nunca volta na resposta
public record UsuarioRequestDTO(

        @NotBlank(message = "Nome completo é obrigatório")
        String nomeCompleto,

        @NotBlank(message = "CPF é obrigatório")
        @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
        String cpf,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String senha,

        String telefone,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,

        @Size(min = 8, max = 8, message = "CEP deve ter 8 dígitos")
        String cep
) {}
