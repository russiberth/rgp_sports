package com.pcs.rgpsports.service;

import com.pcs.rgpsports.exception.ResourceNotFoundException;
import com.pcs.rgpsports.model.Usuario;
import com.pcs.rgpsports.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Retorna todos os usuários
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Busca usuário por ID — lança exceção se não encontrar
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }

    // Busca usuário por e-mail
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com e-mail: " + email));
    }

    // Salva novo usuário — valida e-mail e CPF duplicados
    @Transactional
    public Usuario salvar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado: " + usuario.getEmail());
        }
        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado: " + usuario.getCpf());
        }
        return usuarioRepository.save(usuario);
    }

    // Atualiza dados do usuário
    @Transactional
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = buscarPorId(id);
        usuario.setNomeCompleto(usuarioAtualizado.getNomeCompleto());
        usuario.setTelefone(usuarioAtualizado.getTelefone());
        usuario.setLogradouro(usuarioAtualizado.getLogradouro());
        usuario.setNumero(usuarioAtualizado.getNumero());
        usuario.setComplemento(usuarioAtualizado.getComplemento());
        usuario.setBairro(usuarioAtualizado.getBairro());
        usuario.setCidade(usuarioAtualizado.getCidade());
        usuario.setEstado(usuarioAtualizado.getEstado());
        usuario.setCep(usuarioAtualizado.getCep());
        return usuarioRepository.save(usuario);
    }

    // Remove usuário por ID
    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
