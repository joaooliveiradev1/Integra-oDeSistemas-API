package com.apoiace.api.service;


import com.apoiace.api.models.dtos.*;
import com.apoiace.api.models.entity.Usuario;
import com.apoiace.api.models.Enums.UsuarioRole;
import com.apoiace.api.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        String token = jwtService.gerarToken(usuario.getEmail()); // ← email, não o objeto

        LoginResponseDTO resp = new LoginResponseDTO();
        resp.setToken(token);
        resp.setUsuarioId(usuario.getId());
        resp.setNome(usuario.getNome());
        resp.setEmail(usuario.getEmail());
        resp.setRole(usuario.getRole());
        return resp;
    }

    public LoginResponseDTO registro(UsuarioCreateRequest dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario newUser = new Usuario();
        newUser.setNome(dto.getNome());
        newUser.setEmail(dto.getEmail());
        newUser.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        newUser.setRole(UsuarioRole.APOIADOR);
        newUser.setCpf(dto.getCpf());
        newUser.setDataNascimento(dto.getDataNascimento());

        Usuario saved = usuarioRepository.save(newUser);

        String token = jwtService.gerarToken(saved.getEmail()); // ← email, não o objeto

        LoginResponseDTO resp = new LoginResponseDTO();
        resp.setToken(token);
        resp.setUsuarioId(saved.getId());
        resp.setNome(saved.getNome());
        resp.setEmail(saved.getEmail());
        resp.setRole(saved.getRole());
        return resp;
    }

}

