package com.pcs.rgpsports.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produtos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String time;

    @NotBlank
    @Column(nullable = false)
    private String modalidade;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer estoque;

    @NotBlank
    @Column(nullable = false)
    private String tamanho; // PP, P, M, G, GG, XGG

    @NotBlank
    @Column(nullable = false)
    private String genero; // Masculino, Feminino, Infantil

    @ElementCollection
    @CollectionTable(name = "produto_imagens", joinColumns = @JoinColumn(name = "produto_id"))
    @Column(name = "url_imagem")
    private List<String> imagens;

    @NotNull
    @DecimalMin("0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String temporada;

    @NotBlank
    @Builder.Default
    @Column(nullable = false)
    private String status = "ativo"; // ativo, inativo
}
