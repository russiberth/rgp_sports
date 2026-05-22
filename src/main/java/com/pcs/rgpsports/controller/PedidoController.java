package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.Pedido;
import com.pcs.rgpsports.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // GET /pedidos — lista todos os pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    // GET /pedidos/{id} — busca pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    // GET /pedidos/usuario/{usuarioId} — lista pedidos de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    // POST /pedidos — cria novo pedido
    @PostMapping
    public ResponseEntity<Pedido> salvar(@RequestBody @Valid Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.salvar(pedido));
    }

    // PUT /pedidos/{id}/status — atualiza status do pedido
    @PutMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, status));
    }

    // PUT /pedidos/{id}/cancelar — cancela pedido
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }

    // DELETE /pedidos/{id} — remove pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
