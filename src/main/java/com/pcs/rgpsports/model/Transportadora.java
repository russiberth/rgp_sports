package com.pcs.rgpsports.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "transportadoras")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transportadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    private String telefone;

    @Email
    private String email;

    private String siteApiRastreio;

    private String regioesAtendidas;
}
