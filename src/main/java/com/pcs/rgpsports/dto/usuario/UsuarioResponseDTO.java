package com.pcs.rgpsports.dto.usuario;

import com.pcs.rgpsports.model.Usuario;
import java.time.LocalDateTime;

// O que o cliente RECEBE — sem senha e sem CPF
public record UsuarioResponseDTO(
        Long id,
        String nomeCompleto,
        String email,
        String telefone,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep,
        LocalDateTime dataCadastro,
        String status
) {
        // Converte Entity → DTO — senha e CPF não entram aqui
        public static UsuarioResponseDTO from(Usuario u) {
                return new UsuarioResponseDTO(
                        u.getId(),
                        u.getNomeCompleto(),
                        u.getEmail(),
                        u.getTelefone(),
                        u.getLogradouro(),
                        u.getNumero(),
                        u.getComplemento(),
                        u.getBairro(),
                        u.getCidade(),
                        u.getEstado(),
                        u.getCep(),
                        u.getDataCadastro(),
                        u.getStatus()
                );
        }
}
