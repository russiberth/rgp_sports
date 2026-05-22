package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.ItemPedido;
import com.pcs.rgpsports.service.ItemPedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-pedido")
@RequiredArgsConstructor
public class ItemPedidoController {

    private final ItemPedidoService itemPedidoService;

    // GET /itens-pedido/pedido/{pedidoId} — lista itens de um pedido
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<ItemPedido>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(itemPedidoService.listarPorPedido(pedidoId));
    }

    // GET /itens-pedido/{id} — busca item por ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemPedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(itemPedidoService.buscarPorId(id));
    }

    // POST /itens-pedido — adiciona item ao pedido
    @PostMapping
    public ResponseEntity<ItemPedido> salvar(@RequestBody @Valid ItemPedido item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemPedidoService.salvar(item));
    }

    // PUT /itens-pedido/{id}/quantidade — atualiza quantidade do item
    @PutMapping("/{id}/quantidade")
    public ResponseEntity<ItemPedido> atualizarQuantidade(@PathVariable Long id, @RequestParam Integer quantidade) {
        return ResponseEntity.ok(itemPedidoService.atualizarQuantidade(id, quantidade));
    }

    // DELETE /itens-pedido/{id} — remove item do pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemPedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
