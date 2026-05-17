package com.pcs.rgpsports.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nomeCompleto;

    @NotBlank
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String senha; // armazenada em hash bcrypt

    @Column
    private String telefone;

    // Endereço embutido
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;

    @Column(length = 8)
    private String cep;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private String status = "ativo"; // ativo, inativo

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;
}
