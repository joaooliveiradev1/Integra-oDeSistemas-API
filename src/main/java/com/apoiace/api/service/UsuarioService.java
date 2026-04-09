package com.apoiace.api.service;

import com.apoiace.api.models.Enums.UsuarioRole;
import com.apoiace.api.models.dtos.UsuarioCreateRequest;
import com.apoiace.api.models.dtos.UsuarioUpdateRequest;
import com.apoiace.api.models.entity.Usuario;
import com.apoiace.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario registrarNovoUsuario(UsuarioCreateRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (request.getCpf() != null && usuarioRepository.existsByCpf(request.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenhaHash(passwordEncoder.encode(request.getSenha()));
        usuario.setCpf(request.getCpf());
        usuario.setTelefone(request.getTelefone());
        usuario.setDataNascimento(request.getDataNascimento());
        usuario.setRole(UsuarioRole.APOIADOR);

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = buscarPorId(id);

        if (request.getNome() != null) usuario.setNome(request.getNome());
        if (request.getTelefone() != null) usuario.setTelefone(request.getTelefone());
        if (request.getDataNascimento() != null) usuario.setDataNascimento(request.getDataNascimento());
        if (request.getSenha() != null) usuario.setSenhaHash(passwordEncoder.encode(request.getSenha()));

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setDeletedAt(OffsetDateTime.now());
        usuarioRepository.save(usuario);
    }
}

