package com.apoiace.api.models.dtos;

import com.apoiace.api.models.Enums.UsuarioRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private LocalDate dataNascimento;
    private UsuarioRole role;
}
