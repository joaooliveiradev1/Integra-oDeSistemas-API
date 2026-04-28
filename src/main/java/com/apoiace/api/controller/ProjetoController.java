package com.apoiace.api.controller;

import com.apoiace.api.models.Enums.StatusProjeto;
import com.apoiace.api.models.Enums.TipoAssinatura;
import com.apoiace.api.models.dtos.ProjetoRequestDTO;
import com.apoiace.api.models.dtos.ProjetoResponseDTO;
import com.apoiace.api.models.dtos.ProjetoUpdateDTO;
import com.apoiace.api.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<ProjetoResponseDTO> criar(
            @Valid @RequestBody ProjetoRequestDTO dto,
            @RequestParam String emailUsuario
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projetoService.criar(dto, emailUsuario));
    }

    @GetMapping
    public ResponseEntity<Page<ProjetoResponseDTO>> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) StatusProjeto status,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) TipoAssinatura tipoAssinatura,
            @PageableDefault(size = 12, sort = "criadoEm") Pageable pageable
    ) {
        return ResponseEntity.ok(
                projetoService.listar(status, categoriaId, tipoAssinatura, titulo, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(projetoService.buscarPorId(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorSlug(@PathVariable String slug) {
        return ResponseEntity.ok(projetoService.buscarPorSlug(slug));
    }

    @GetMapping("/criador/{criadorId}")
    public ResponseEntity<Page<ProjetoResponseDTO>> listarPorCriador(
            @PathVariable Long criadorId,
            @RequestParam(required = false) StatusProjeto status,
            @PageableDefault(size = 12, sort = "criadoEm") Pageable pageable
    ) {
        return ResponseEntity.ok(
                projetoService.listarPorCriador(criadorId, status, pageable)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProjetoUpdateDTO dto,
            @RequestParam String emailUsuario
    ) {
        return ResponseEntity.ok(
                projetoService.atualizar(id, dto, emailUsuario, true)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjetoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusProjeto status,
            @RequestParam String emailUsuario
    ) {
        return ResponseEntity.ok(
                projetoService.atualizarStatus(id, status, emailUsuario, true)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestParam String emailUsuario
    ) {
        projetoService.deletar(id, emailUsuario, true);
        return ResponseEntity.noContent().build();
    }
}