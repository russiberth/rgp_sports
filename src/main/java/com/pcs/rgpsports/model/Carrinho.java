package com.pcs.rgpsports.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carrinhos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dataUltimaModificacao = LocalDateTime.now();
}
