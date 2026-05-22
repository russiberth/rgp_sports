package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.Produto;
import com.pcs.rgpsports.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    // GET /produtos — lista todos os produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    // GET /produtos/{id} — busca produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    // GET /produtos/modalidade/{modalidade} — filtra por modalidade
    @GetMapping("/modalidade/{modalidade}")
    public ResponseEntity<List<Produto>> buscarPorModalidade(@PathVariable String modalidade) {
        return ResponseEntity.ok(produtoService.buscarPorModalidade(modalidade));
    }

    // GET /produtos/time/{time} — busca por nome do time
    @GetMapping("/time/{time}")
    public ResponseEntity<List<Produto>> buscarPorTime(@PathVariable String time) {
        return ResponseEntity.ok(produtoService.buscarPorTime(time));
    }

    // POST /produtos — cadastra novo produto
    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody @Valid Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvar(produto));
    }

    // PUT /produtos/{id} — atualiza produto existente
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody @Valid Produto produto) {
        return ResponseEntity.ok(produtoService.atualizar(id, produto));
    }

    // DELETE /produtos/{id} — remove produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
