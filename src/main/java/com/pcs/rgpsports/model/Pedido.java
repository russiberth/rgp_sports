package com.pcs.rgpsports.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Builder.Default
    @Column(precision = 10, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Builder.Default
    @Column(nullable = false)
    private String status = "pendente"; // pendente, pago, enviado, entregue, cancelado

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dataPedido = LocalDateTime.now();

    // Endereço de entrega
    private String enderecoEntrega;

    @ManyToOne
    @JoinColumn(name = "cupom_id")
    private Cupom cupom;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Entrega entrega;
}
