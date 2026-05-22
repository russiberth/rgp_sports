package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.Carrinho;
import com.pcs.rgpsports.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    // GET /carrinho/usuario/{usuarioId} — retorna carrinho do usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrinho> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carrinhoService.buscarPorUsuario(usuarioId));
    }

    // POST /carrinho/usuario/{usuarioId}/produto/{produtoId} — adiciona item ao carrinho
    @PostMapping("/usuario/{usuarioId}/produto/{produtoId}")
    public ResponseEntity<Carrinho> adicionarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long produtoId,
            @RequestParam Integer quantidade) {
        return ResponseEntity.ok(carrinhoService.adicionarItem(usuarioId, produtoId, quantidade));
    }

    // DELETE /carrinho/usuario/{usuarioId}/produto/{produtoId} — remove item do carrinho
    @DeleteMapping("/usuario/{usuarioId}/produto/{produtoId}")
    public ResponseEntity<Void> removerItem(
            @PathVariable Long usuarioId,
            @PathVariable Long produtoId) {
        carrinhoService.removerItem(usuarioId, produtoId);
        return ResponseEntity.noContent().build();
    }

    // DELETE /carrinho/usuario/{usuarioId}/limpar — limpa todo o carrinho
    @DeleteMapping("/usuario/{usuarioId}/limpar")
    public ResponseEntity<Void> limparCarrinho(@PathVariable Long usuarioId) {
        carrinhoService.limparCarrinho(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
