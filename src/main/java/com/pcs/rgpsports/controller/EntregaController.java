package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.Entrega;
import com.pcs.rgpsports.service.EntregaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregaController {

    private final EntregaService entregaService;

    // GET /entregas — lista todas as entregas
    @GetMapping
    public ResponseEntity<List<Entrega>> listarTodos() {
        return ResponseEntity.ok(entregaService.listarTodos());
    }

    // GET /entregas/{id} — busca entrega por ID
    @GetMapping("/{id}")
    public ResponseEntity<Entrega> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entregaService.buscarPorId(id));
    }

    // GET /entregas/pedido/{pedidoId} — busca entrega pelo pedido
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Entrega> buscarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(entregaService.buscarPorPedido(pedidoId));
    }

    // GET /entregas/rastreio/{codigo} — rastreia entrega pelo código
    @GetMapping("/rastreio/{codigo}")
    public ResponseEntity<Entrega> buscarPorRastreio(@PathVariable String codigo) {
        return ResponseEntity.ok(entregaService.buscarPorRastreio(codigo));
    }

    // POST /entregas — registra nova entrega
    @PostMapping
    public ResponseEntity<Entrega> salvar(@RequestBody @Valid Entrega entrega) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entregaService.salvar(entrega));
    }

    // PUT /entregas/{id}/status — atualiza status da entrega
    @PutMapping("/{id}/status")
    public ResponseEntity<Entrega> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(entregaService.atualizarStatus(id, status));
    }

    // PUT /entregas/{id}/rastreio — adiciona código de rastreio
    @PutMapping("/{id}/rastreio")
    public ResponseEntity<Entrega> adicionarRastreio(@PathVariable Long id, @RequestParam String codigo) {
        return ResponseEntity.ok(entregaService.adicionarRastreio(id, codigo));
    }

    // DELETE /entregas/{id} — remove entrega
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        entregaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
