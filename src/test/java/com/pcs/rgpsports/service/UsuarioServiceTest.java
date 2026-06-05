package com.pcs.rgpsports.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Usuario;
import com.pcs.rgpsports.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario exemplo() {
        return Usuario.builder()
                .id(1L)
                .nomeCompleto("João Silva")
                .cpf("12345678900")
                .email("joao@email.com")
                .senha("hash123")
                .status("ativo")
                .build();
    }

    @Test
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(exemplo()));
        assertThat(service.listarTodos()).hasSize(1);
    }

    @Test
    void deveBuscarPorIdQuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(exemplo()));
        assertThat(service.buscarPorId(1L).getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    void deveLancar404QuandoNaoEncontrarPorId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveBuscarPorEmail() {
        when(repository.findByEmail("joao@email.com")).thenReturn(Optional.of(exemplo()));
        assertThat(service.buscarPorEmail("joao@email.com").getId()).isEqualTo(1L);
    }

    @Test
    void deveLancar404QuandoNaoEncontrarPorEmail() {
        when(repository.findByEmail("x@x.com")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorEmail("x@x.com"));
    }

    @Test
    void deveSalvarUsuarioNovo() {
        Usuario u = exemplo();
        when(repository.existsByEmail("joao@email.com")).thenReturn(false);
        when(repository.existsByCpf("12345678900")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(u);
        assertThat(service.salvar(u).getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    void naoDeveSalvarComEmailDuplicado() {
        Usuario u = exemplo();
        when(repository.existsByEmail("joao@email.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(u));
        verify(repository, never()).save(any());
    }

    @Test
    void naoDeveSalvarComCpfDuplicado() {
        Usuario u = exemplo();
        when(repository.existsByEmail("joao@email.com")).thenReturn(false);
        when(repository.existsByCpf("12345678900")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(u));
        verify(repository, never()).save(any());
    }

    @Test
    void deveDeletarUsuario() {
        Usuario u = exemplo();
        when(repository.findById(1L)).thenReturn(Optional.of(u));
        service.deletar(1L);
        verify(repository).delete(u);
    }
}