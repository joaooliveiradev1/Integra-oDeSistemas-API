package com.apoiace.api.service;

import com.apoiace.api.models.dtos.CategoriaRequestDTO;
import com.apoiace.api.models.dtos.CategoriaResponseDTO;
import com.apoiace.api.models.entity.Categoria;
import com.apoiace.api.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }

    public List<CategoriaResponseDTO> buscarPorNome(String nome) {
        return categoriaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        return categoriaRepository.findByIdAndDeletedAtIsNull(id)
                .map(CategoriaResponseDTO::fromEntity)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));
    }

    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO request) {
        if (categoriaRepository.existsByNomeIgnoreCaseAndDeletedAtIsNull(request.getNome())) {
            throw new IllegalArgumentException("Já existe uma categoria com esse nome");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(request.getNome());
        categoria.setCor(request.getCor());

        return CategoriaResponseDTO.fromEntity(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO request) {
        Categoria categoria = categoriaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));

        if (categoriaRepository.existsByNomeIgnoreCaseAndDeletedAtIsNullAndIdNot(request.getNome(), id)) {
            throw new IllegalArgumentException("Já existe uma categoria com esse nome");
        }

        categoria.setNome(request.getNome());
        categoria.setCor(request.getCor());

        return CategoriaResponseDTO.fromEntity(categoriaRepository.save(categoria));
    }

    @Transactional
    public void deletar(Long id) {
        Categoria categoria = categoriaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));

        categoria.setDeletedAt(OffsetDateTime.now());
        categoriaRepository.save(categoria);
    }
}
