package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.dto.pagamento.PagamentoRequestDTO;
import com.pcs.rgpsports.dto.pagamento.PagamentoResponseDTO;
import com.pcs.rgpsports.model.Pagamento;
import com.pcs.rgpsports.model.Pedido;
import com.pcs.rgpsports.service.PagamentoService;
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
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Operações de pagamento e estorno")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Operation(summary = "Lista todos os pagamentos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> listarTodos() {
        List<PagamentoResponseDTO> lista = pagamentoService.listarTodos()
                .stream()
                .map(PagamentoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca pagamento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagamento encontrado"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PagamentoResponseDTO.from(pagamentoService.buscarPorId(id)));
    }

    @Operation(summary = "Busca pagamento pelo pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagamento encontrado"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<PagamentoResponseDTO> buscarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(PagamentoResponseDTO.from(pagamentoService.buscarPorPedido(pedidoId)));
    }

    @Operation(summary = "Registra novo pagamento")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pagamento registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> salvar(@RequestBody @Valid PagamentoRequestDTO dto) {
        // Converte RequestDTO → Entity
        Pagamento pagamento = new Pagamento();

        Pedido pedido = new Pedido();
        pedido.setId(dto.pedidoId());
        pagamento.setPedido(pedido);
        pagamento.setMetodo(dto.metodo());
        pagamento.setValor(dto.valor());
        pagamento.setParcelas(dto.parcelas() != null ? dto.parcelas() : 1);

        Pagamento salvo = pagamentoService.salvar(pagamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(PagamentoResponseDTO.from(salvo));
    }

    @Operation(summary = "Atualiza status da transação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<PagamentoResponseDTO> atualizarStatus(@PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(PagamentoResponseDTO.from(pagamentoService.atualizarStatus(id, status)));
    }

    @Operation(summary = "Estorna pagamento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagamento estornado"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado"),
        @ApiResponse(responseCode = "400", description = "Pagamento não pode ser estornado")
    })
    @PutMapping("/{id}/estornar")
    public ResponseEntity<PagamentoResponseDTO> estornar(@PathVariable Long id) {
        return ResponseEntity.ok(PagamentoResponseDTO.from(pagamentoService.estornar(id)));
    }

    @Operation(summary = "Remove pagamento")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pagamento removido"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
