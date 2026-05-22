package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.model.Transportadora;
import com.pcs.rgpsports.service.TransportadoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transportadoras")
@RequiredArgsConstructor
public class TransportadoraController {

    private final TransportadoraService transportadoraService;

    // GET /transportadoras — lista todas as transportadoras
    @GetMapping
    public ResponseEntity<List<Transportadora>> listarTodos() {
        return ResponseEntity.ok(transportadoraService.listarTodos());
    }

    // GET /transportadoras/{id} — busca transportadora por ID
    @GetMapping("/{id}")
    public ResponseEntity<Transportadora> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transportadoraService.buscarPorId(id));
    }

    // POST /transportadoras — cadastra nova transportadora
    @PostMapping
    public ResponseEntity<Transportadora> salvar(@RequestBody @Valid Transportadora transportadora) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transportadoraService.salvar(transportadora));
    }

    // PUT /transportadoras/{id} — atualiza transportadora existente
    @PutMapping("/{id}")
    public ResponseEntity<Transportadora> atualizar(@PathVariable Long id,
            @RequestBody @Valid Transportadora transportadora) {
        return ResponseEntity.ok(transportadoraService.atualizar(id, transportadora));
    }

    // DELETE /transportadoras/{id} — remove transportadora
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transportadoraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
