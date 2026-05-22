package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.ItemCarrinho;
import com.pcs.rgpsports.service.ItemCarrinhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-carrinho")
@RequiredArgsConstructor
public class ItemCarrinhoController {

    private final ItemCarrinhoService itemCarrinhoService;

    // GET /itens-carrinho/carrinho/{carrinhoId} — lista itens de um carrinho
    @GetMapping("/carrinho/{carrinhoId}")
    public ResponseEntity<List<ItemCarrinho>> listarPorCarrinho(@PathVariable Long carrinhoId) {
        return ResponseEntity.ok(itemCarrinhoService.listarPorCarrinho(carrinhoId));
    }

    // GET /itens-carrinho/{id} — busca item por ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemCarrinho> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(itemCarrinhoService.buscarPorId(id));
    }

    // POST /itens-carrinho — adiciona item ao carrinho
    @PostMapping
    public ResponseEntity<ItemCarrinho> salvar(@RequestBody @Valid ItemCarrinho item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemCarrinhoService.salvar(item));
    }

    // PUT /itens-carrinho/{id}/quantidade — atualiza quantidade do item
    @PutMapping("/{id}/quantidade")
    public ResponseEntity<ItemCarrinho> atualizarQuantidade(@PathVariable Long id, @RequestParam Integer quantidade) {
        return ResponseEntity.ok(itemCarrinhoService.atualizarQuantidade(id, quantidade));
    }

    // DELETE /itens-carrinho/{id} — remove item do carrinho
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemCarrinhoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
