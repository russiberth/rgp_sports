package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.Pagamento;
import com.pcs.rgpsports.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    // GET /pagamentos — lista todos os pagamentos
    @GetMapping
    public ResponseEntity<List<Pagamento>> listarTodos() {
        return ResponseEntity.ok(pagamentoService.listarTodos());
    }

    // GET /pagamentos/{id} — busca pagamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.buscarPorId(id));
    }

    // GET /pagamentos/pedido/{pedidoId} — busca pagamento pelo pedido
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Pagamento> buscarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pagamentoService.buscarPorPedido(pedidoId));
    }

    // POST /pagamentos — registra novo pagamento
    @PostMapping
    public ResponseEntity<Pagamento> salvar(@RequestBody @Valid Pagamento pagamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.salvar(pagamento));
    }

    // PUT /pagamentos/{id}/status — atualiza status da transação
    @PutMapping("/{id}/status")
    public ResponseEntity<Pagamento> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(pagamentoService.atualizarStatus(id, status));
    }

    // PUT /pagamentos/{id}/estornar — estorna pagamento
    @PutMapping("/{id}/estornar")
    public ResponseEntity<Pagamento> estornar(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.estornar(id));
    }

    // DELETE /pagamentos/{id} — remove pagamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
