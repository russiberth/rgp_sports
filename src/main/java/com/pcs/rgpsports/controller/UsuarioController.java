package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.dto.usuario.UsuarioRequestDTO;
import com.pcs.rgpsports.dto.usuario.UsuarioResponseDTO;
import com.pcs.rgpsports.model.Usuario;
import com.pcs.rgpsports.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operações de cadastro e gestão de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Lista todos os usuários")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> lista = usuarioService.listarTodos()
                .stream()
                .map(UsuarioResponseDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca usuário por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponseDTO.from(usuarioService.buscarPorId(id)));
    }

    @Operation(summary = "Busca usuário por e-mail")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(UsuarioResponseDTO.from(usuarioService.buscarPorEmail(email)));
    }

    @Operation(summary = "Cadastra novo usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "E-mail ou CPF já cadastrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvar(@RequestBody @Valid UsuarioRequestDTO dto) {
        // Converte RequestDTO → Entity
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.nomeCompleto());
        usuario.setCpf(dto.cpf());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());
        usuario.setTelefone(dto.telefone());
        usuario.setLogradouro(dto.logradouro());
        usuario.setNumero(dto.numero());
        usuario.setComplemento(dto.complemento());
        usuario.setBairro(dto.bairro());
        usuario.setCidade(dto.cidade());
        usuario.setEstado(dto.estado());
        usuario.setCep(dto.cep());

        Usuario salvo = usuarioService.salvar(usuario);
        // ResponseDTO não inclui senha nem CPF
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDTO.from(salvo));
    }

    @Operation(summary = "Atualiza dados do usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.nomeCompleto());
        usuario.setTelefone(dto.telefone());
        usuario.setLogradouro(dto.logradouro());
        usuario.setNumero(dto.numero());
        usuario.setComplemento(dto.complemento());
        usuario.setBairro(dto.bairro());
        usuario.setCidade(dto.cidade());
        usuario.setEstado(dto.estado());
        usuario.setCep(dto.cep());

        return ResponseEntity.ok(UsuarioResponseDTO.from(usuarioService.atualizar(id, usuario)));
    }

    @Operation(summary = "Remove usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário removido"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
