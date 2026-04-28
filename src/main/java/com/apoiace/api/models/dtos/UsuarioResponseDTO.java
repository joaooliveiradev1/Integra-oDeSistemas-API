package com.apoiace.api.models.dtos;

import com.apoiace.api.models.Enums.UsuarioRole;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private UsuarioRole role;
    private OffsetDateTime criadoEm;
}
