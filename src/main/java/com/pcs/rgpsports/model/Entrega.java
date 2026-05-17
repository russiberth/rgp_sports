package com.pcs.rgpsports.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "entregas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "transportadora_id")
    private Transportadora transportadora;

    private String codigoRastreio;

    private LocalDate prazoEstimado;

    @Builder.Default
    @Column(nullable = false)
    private String statusEntrega = "aguardando"; // aguardando, enviado, em_transito, entregue

    private LocalDateTime dataEnvio;

    private LocalDateTime dataEntrega;
}
