package com.pcs.rgpsports.controller;

import com.pcs.rgpsports.dto.pedido.PedidoRequestDTO;
import com.pcs.rgpsports.dto.pedido.PedidoResponseDTO;
import com.pcs.rgpsports.model.Pedido;
import com.pcs.rgpsports.model.Usuario;
import com.pcs.rgpsports.model.Cupom;
import com.pcs.rgpsports.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Operações de pedidos e checkout")
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Lista todos os pedidos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarTodos() {
        List<PedidoResponseDTO> lista = pedidoService.listarTodos()
                .stream()
                .map(PedidoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca pedido por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoResponseDTO.from(pedidoService.buscarPorId(id)));
    }

    @Operation(summary = "Lista pedidos de um usuário")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<PedidoResponseDTO> lista = pedidoService.listarPorUsuario(usuarioId)
                .stream()
                .map(PedidoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Cria novo pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> salvar(@RequestBody @Valid PedidoRequestDTO dto) {
        // Converte RequestDTO → Entity
        Pedido pedido = new Pedido();

        Usuario usuario = new Usuario();
        usuario.setId(dto.usuarioId());
        pedido.setUsuario(usuario);
        pedido.setEnderecoEntrega(dto.enderecoEntrega());
        pedido.setSubtotal(BigDecimal.ZERO);
        pedido.setDesconto(BigDecimal.ZERO);
        pedido.setValorTotal(BigDecimal.ZERO);

        if (dto.cupomId() != null) {
            Cupom cupom = new Cupom();
            cupom.setId(dto.cupomId());
            pedido.setCupom(cupom);
        }

        Pedido salvo = pedidoService.salvar(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoResponseDTO.from(salvo));
    }

    @Operation(summary = "Atualiza status do pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(PedidoResponseDTO.from(pedidoService.atualizarStatus(id, status)));
    }

    @Operation(summary = "Cancela pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido cancelado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Pedido não pode ser cancelado")
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoResponseDTO.from(pedidoService.cancelar(id)));
    }

    @Operation(summary = "Remove pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pedido removido"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
