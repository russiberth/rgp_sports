package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.Usuario;
import com.pcs.rgpsports.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GET /usuarios — lista todos os usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // GET /usuarios/{id} — busca usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // GET /usuarios/email/{email} — busca usuário por e-mail
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    // POST /usuarios — cadastra novo usuário
    @PostMapping
    public ResponseEntity<Usuario> salvar(@RequestBody @Valid Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(usuario));
    }

    // PUT /usuarios/{id} — atualiza dados do usuário
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody @Valid Usuario usuario) {
        return ResponseEntity.ok(usuarioService.atualizar(id, usuario));
    }

    // DELETE /usuarios/{id} — remove usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
