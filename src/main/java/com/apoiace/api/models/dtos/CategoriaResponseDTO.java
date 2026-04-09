package com.apoiace.api.models.dtos;

public class CategoriaResponseDTO{
}
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