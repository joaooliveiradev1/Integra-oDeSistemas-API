package com.apoiace.api.models.dtos;

import com.apoiace.api.models.entity.Categoria;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class CategoriaResponseDTO {

    private Long id;
    private String nome;
    private String cor;
    private OffsetDateTime criadoEm;

    public static CategoriaResponseDTO fromEntity(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setCor(categoria.getCor());
        dto.setCriadoEm(categoria.getCriadoEm());
        return dto;
    }
}
