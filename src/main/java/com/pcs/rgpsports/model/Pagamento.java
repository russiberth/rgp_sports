package com.pcs.rgpsports.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @NotBlank
    @Column(nullable = false)
    private String metodo; // PIX, CARTAO, BOLETO

    @NotBlank
    @Builder.Default
    @Column(nullable = false)
    private String statusTransacao = "pendente"; // pendente, autorizada, capturada, recusada, estornada

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dataPagamento = LocalDateTime.now();

    @Column(unique = true)
    private String idTransacaoGateway;

    @Builder.Default
    private Integer parcelas = 1;
}
