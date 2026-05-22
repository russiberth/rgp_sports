package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.Cupom;
import com.pcs.rgpsports.service.CupomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cupons")
@RequiredArgsConstructor
public class CupomController {

    private final CupomService cupomService;

    // GET /cupons — lista todos os cupons
    @GetMapping
    public ResponseEntity<List<Cupom>> listarTodos() {
        return ResponseEntity.ok(cupomService.listarTodos());
    }

    // GET /cupons/{id} — busca cupom por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cupom> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cupomService.buscarPorId(id));
    }

    // GET /cupons/validar/{codigo} — valida se cupom pode ser usado
    @GetMapping("/validar/{codigo}")
    public ResponseEntity<Cupom> validar(@PathVariable String codigo) {
        return ResponseEntity.ok(cupomService.validarCupom(codigo));
    }

    // POST /cupons — cadastra novo cupom
    @PostMapping
    public ResponseEntity<Cupom> salvar(@RequestBody @Valid Cupom cupom) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cupomService.salvar(cupom));
    }

    // PUT /cupons/{id} — atualiza cupom existente
    @PutMapping("/{id}")
    public ResponseEntity<Cupom> atualizar(@PathVariable Long id, @RequestBody @Valid Cupom cupom) {
        return ResponseEntity.ok(cupomService.atualizar(id, cupom));
    }

    // DELETE /cupons/{id} — remove cupom
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cupomService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
