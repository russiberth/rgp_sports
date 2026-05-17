package com.pcs.rgpsports.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cupons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String codigo;

    @NotBlank
    @Column(nullable = false)
    private String tipo; // PERCENTUAL, VALOR_FIXO

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorDesconto;

    @NotNull
    @Column(nullable = false)
    private LocalDate validade;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer limiteUso;

    @Builder.Default
    @Column(nullable = false)
    private Integer quantidadeUsada = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorMinimoPedido;

    @Builder.Default
    @Column(nullable = false)
    private String status = "ativo"; // ativo, inativo
}
