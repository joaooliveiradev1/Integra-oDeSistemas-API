package com.apoiace.api.models.dtos;

import com.apoiace.api.models.Enums.UsuarioRole;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginResponseDTO {

    private String token;
    private String usuarioId;
    private String nome;
    private String email;
    private UsuarioRole role;

    public void setUsuarioId(Long id) {
    }
}

