package com.apoiace.api.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioUpdateRequest {
    private String nome;
    private String telefone;
    private LocalDate dataNascimento;
    private String senha;
}

