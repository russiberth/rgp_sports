package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.dto.cupom.CupomRequestDTO;
import com.pcs.rgpsports.dto.cupom.CupomResponseDTO;
import com.pcs.rgpsports.model.Cupom;
import com.pcs.rgpsports.service.CupomService;
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
@RequestMapping("/cupons")
@RequiredArgsConstructor
@Tag(name = "Cupons", description = "Operações de cupons de desconto")
public class CupomController {

    private final CupomService cupomService;

    @Operation(summary = "Lista todos os cupons")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<CupomResponseDTO>> listarTodos() {
        List<CupomResponseDTO> lista = cupomService.listarTodos()
                .stream()
                .map(CupomResponseDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca cupom por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cupom encontrado"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(CupomResponseDTO.from(cupomService.buscarPorId(id)));
    }

    @Operation(summary = "Valida se cupom pode ser usado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cupom válido"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
        @ApiResponse(responseCode = "400", description = "Cupom inválido ou expirado")
    })
    @GetMapping("/validar/{codigo}")
    public ResponseEntity<CupomResponseDTO> validar(@PathVariable String codigo) {
        return ResponseEntity.ok(CupomResponseDTO.from(cupomService.validarCupom(codigo)));
    }

    @Operation(summary = "Cadastra novo cupom")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Código de cupom já existe")
    })
    @PostMapping
    public ResponseEntity<CupomResponseDTO> salvar(@RequestBody @Valid CupomRequestDTO dto) {
        // Converte RequestDTO → Entity
        Cupom cupom = new Cupom();
        cupom.setCodigo(dto.codigo());
        cupom.setTipo(dto.tipo());
        cupom.setValorDesconto(dto.valorDesconto());
        cupom.setValidade(dto.validade());
        cupom.setLimiteUso(dto.limiteUso());
        cupom.setValorMinimoPedido(dto.valorMinimoPedido());
        cupom.setStatus("ativo");

        Cupom salvo = cupomService.salvar(cupom);
        return ResponseEntity.status(HttpStatus.CREATED).body(CupomResponseDTO.from(salvo));
    }

    @Operation(summary = "Atualiza cupom existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cupom atualizado"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid CupomRequestDTO dto) {
        Cupom cupom = new Cupom();
        cupom.setTipo(dto.tipo());
        cupom.setValorDesconto(dto.valorDesconto());
        cupom.setValidade(dto.validade());
        cupom.setLimiteUso(dto.limiteUso());
        cupom.setValorMinimoPedido(dto.valorMinimoPedido());

        return ResponseEntity.ok(CupomResponseDTO.from(cupomService.atualizar(id, cupom)));
    }

    @Operation(summary = "Remove cupom")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cupom removido"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cupomService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
