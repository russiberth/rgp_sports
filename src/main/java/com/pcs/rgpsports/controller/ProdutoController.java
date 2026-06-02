package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.dto.produto.ProdutoRequestDTO;
import com.pcs.rgpsports.dto.produto.ProdutoResponseDTO;
import com.pcs.rgpsports.model.Produto;
import com.pcs.rgpsports.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Operações de catálogo de camisas esportivas")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(summary = "Lista todos os produtos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<ProdutoResponseDTO> lista = produtoService.listarTodos()
                .stream()
                .map(ProdutoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca produto por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ProdutoResponseDTO.from(produtoService.buscarPorId(id)));
    }

    @Operation(summary = "Busca produtos por modalidade")
    @GetMapping("/modalidade/{modalidade}")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorModalidade(@PathVariable String modalidade) {
        List<ProdutoResponseDTO> lista = produtoService.buscarPorModalidade(modalidade)
                .stream()
                .map(ProdutoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca produtos por time")
    @GetMapping("/time/{time}")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorTime(@PathVariable String time) {
        List<ProdutoResponseDTO> lista = produtoService.buscarPorTime(time)
                .stream()
                .map(ProdutoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Cadastra novo produto")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvar(@RequestBody @Valid ProdutoRequestDTO dto) {
        // Converte RequestDTO → Entity
        Produto produto = new Produto();
        produto.setTime(dto.time());
        produto.setModalidade(dto.modalidade());
        produto.setEstoque(dto.estoque());
        produto.setTamanho(dto.tamanho());
        produto.setGenero(dto.genero());
        produto.setImagens(dto.imagens());
        produto.setPreco(dto.preco());
        produto.setDescricao(dto.descricao());
        produto.setTemporada(dto.temporada());
        produto.setStatus("ativo");

        Produto salvo = produtoService.salvar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponseDTO.from(salvo));
    }

    @Operation(summary = "Atualiza produto existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto atualizado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setTime(dto.time());
        produto.setModalidade(dto.modalidade());
        produto.setEstoque(dto.estoque());
        produto.setTamanho(dto.tamanho());
        produto.setGenero(dto.genero());
        produto.setImagens(dto.imagens());
        produto.setPreco(dto.preco());
        produto.setDescricao(dto.descricao());
        produto.setTemporada(dto.temporada());
        produto.setStatus("ativo");

        return ResponseEntity.ok(ProdutoResponseDTO.from(produtoService.atualizar(id, produto)));
    }

    @Operation(summary = "Remove produto")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Produto removido"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
