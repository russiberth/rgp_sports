package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.Avaliacao;
import com.pcs.rgpsports.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    // GET /avaliacoes — lista todas as avaliações
    @GetMapping
    public ResponseEntity<List<Avaliacao>> listarTodos() {
        return ResponseEntity.ok(avaliacaoService.listarTodos());
    }

    // GET /avaliacoes/{id} — busca avaliação por ID
    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }

    // GET /avaliacoes/produto/{produtoId} — lista avaliações de um produto
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<Avaliacao>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(avaliacaoService.listarPorProduto(produtoId));
    }

    // GET /avaliacoes/produto/{produtoId}/media — retorna média de notas do produto
    @GetMapping("/produto/{produtoId}/media")
    public ResponseEntity<Double> mediaPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(avaliacaoService.mediaNotasPorProduto(produtoId));
    }

    // POST /avaliacoes — cadastra nova avaliação
    @PostMapping
    public ResponseEntity<Avaliacao> salvar(@RequestBody @Valid Avaliacao avaliacao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoService.salvar(avaliacao));
    }

    // PUT /avaliacoes/{id} — atualiza avaliação existente
    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(@PathVariable Long id, @RequestBody @Valid Avaliacao avaliacao) {
        return ResponseEntity.ok(avaliacaoService.atualizar(id, avaliacao));
    }

    // DELETE /avaliacoes/{id} — remove avaliação
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
