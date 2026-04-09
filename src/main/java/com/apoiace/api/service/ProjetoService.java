package com.apoiace.api.service;

import com.apoiace.api.models.Enums.StatusProjeto;
import com.apoiace.api.models.Enums.TipoAssinatura;
import com.apoiace.api.models.dtos.ProjetoRequestDTO;
import com.apoiace.api.models.dtos.ProjetoResponseDTO;
import com.apoiace.api.models.dtos.ProjetoUpdateDTO;
import com.apoiace.api.models.entity.Categoria;
import com.apoiace.api.models.entity.Projeto;
import com.apoiace.api.models.entity.Usuario;
import com.apoiace.api.repository.CategoriaRepository;
import com.apoiace.api.repository.ProjetoRepository;
import com.apoiace.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    // ─── CREATE ───────────────────────────────────────────────────

    @Transactional
    public ProjetoResponseDTO criar(ProjetoRequestDTO dto, String email) {

        Usuario criador = buscarUsuarioPorEmail(email);
        Categoria categoria = buscarCategoria(dto.getCategoriaId());
        validarSlugUnico(dto.getTitulo(), null);

        Projeto projeto = new Projeto();
        projeto.setTitulo(dto.getTitulo());
        projeto.setDescricao(dto.getDescricao());
        projeto.setMetaValor(dto.getMetaValor());
        projeto.setDataFim(dto.getDataFim());
        projeto.setTipoAssinatura(dto.getTipoAssinatura());
        projeto.setVideoUrl(dto.getVideoUrl());
        projeto.setCapaUrl(dto.getCapaUrl());
        projeto.setCriador(criador);
        projeto.setCategoria(categoria);

        return ProjetoResponseDTO.from(projetoRepository.save(projeto));
    }

    // ─── READ ─────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<ProjetoResponseDTO> listar(
            StatusProjeto status,
            Long categoriaId,
            TipoAssinatura tipoAssinatura,
            String titulo,
            Pageable pageable
    ) {
        if (titulo != null && !titulo.isBlank() && status != null) {
            return projetoRepository
                    .findByStatusAndTituloContainingIgnoreCase(status, titulo, pageable)
                    .map(ProjetoResponseDTO::from);
        }
        if (titulo != null && !titulo.isBlank()) {
            return projetoRepository
                    .findByTituloContainingIgnoreCase(titulo, pageable)
                    .map(ProjetoResponseDTO::from);
        }
        if (status != null && categoriaId != null) {
            return projetoRepository
                    .findByStatusAndCategoriaId(status, categoriaId, pageable)
                    .map(ProjetoResponseDTO::from);
        }
        if (status != null && tipoAssinatura != null) {
            return projetoRepository
                    .findByStatusAndTipoAssinatura(status, tipoAssinatura, pageable)
                    .map(ProjetoResponseDTO::from);
        }
        if (status != null) {
            return projetoRepository
                    .findByStatus(status, pageable)
                    .map(ProjetoResponseDTO::from);
        }
        if (categoriaId != null) {
            return projetoRepository
                    .findByCategoriaId(categoriaId, pageable)
                    .map(ProjetoResponseDTO::from);
        }

        return projetoRepository.findAll(pageable).map(ProjetoResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public ProjetoResponseDTO buscarPorId(Long id) {
        return ProjetoResponseDTO.from(buscarProjeto(id));
    }

    @Transactional(readOnly = true)
    public ProjetoResponseDTO buscarPorSlug(String slug) {
        return ProjetoResponseDTO.from(
                projetoRepository.findBySlug(slug)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Projeto não encontrado"))
        );
    }

    @Transactional(readOnly = true)
    public Page<ProjetoResponseDTO> listarPorCriador(Long criadorId, StatusProjeto status, Pageable pageable) {
        if (status != null) {
            return projetoRepository
                    .findByCriadorIdAndStatus(criadorId, status, pageable)
                    .map(ProjetoResponseDTO::from);
        }
        return projetoRepository
                .findByCriadorId(criadorId, pageable)
                .map(ProjetoResponseDTO::from);
    }

    // ─── UPDATE ───────────────────────────────────────────────────

    @Transactional
    public ProjetoResponseDTO atualizar(Long id, ProjetoUpdateDTO dto, String criadorEmail, boolean isAdmin) {

        Projeto projeto = buscarProjeto(id);
        Usuario solicitante = buscarUsuarioPorEmail(criadorEmail);

        validarPropriedade(projeto, solicitante.getId(), isAdmin);
        validarSlugUnico(dto.getTitulo(), id);

        Categoria categoria = buscarCategoria(dto.getCategoriaId());

        projeto.setTitulo(dto.getTitulo());
        projeto.setDescricao(dto.getDescricao());
        projeto.setMetaValor(dto.getMetaValor());
        projeto.setDataFim(dto.getDataFim());
        projeto.setTipoAssinatura(dto.getTipoAssinatura());
        projeto.setVideoUrl(dto.getVideoUrl());
        projeto.setCapaUrl(dto.getCapaUrl());
        projeto.setCategoria(categoria);

        if (dto.getStatus() != null && isAdmin) {
            projeto.setStatus(dto.getStatus());
        }

        return ProjetoResponseDTO.from(projetoRepository.save(projeto));
    }

    // ─── STATUS ───────────────────────────────────────────────────

    @Transactional
    public ProjetoResponseDTO atualizarStatus(Long id, StatusProjeto novoStatus, String criadorEmail, boolean isAdmin) {
        Projeto projeto = buscarProjeto(id);
        Usuario solicitante = buscarUsuarioPorEmail(criadorEmail); // ← erro 2 corrigido

        validarPropriedade(projeto, solicitante.getId(), isAdmin);
        projeto.setStatus(novoStatus);
        return ProjetoResponseDTO.from(projetoRepository.save(projeto));
    }

    // ─── DELETE ───────────────────────────────────────────────────

    @Transactional
    public void deletar(Long id, String criadorEmail, boolean isAdmin) {
        Projeto projeto = buscarProjeto(id);
        Usuario solicitante = buscarUsuarioPorEmail(criadorEmail); // ← erro 3 corrigido

        validarPropriedade(projeto, solicitante.getId(), isAdmin);
        projeto.softDelete();
        projetoRepository.save(projeto);
    }

    // ─── ENCERRAMENTO AUTOMÁTICO ──────────────────────────────────

    @Transactional
    public void encerrarProjetosExpirados() {
        projetoRepository
                .findByStatusAndDataFimBefore(StatusProjeto.PUBLICADO, LocalDate.now(), Pageable.unpaged())
                .forEach(p -> {
                    p.setStatus(StatusProjeto.ENCERRADO);
                    projetoRepository.save(p);
                });
    }

    // ─── Helpers privados ─────────────────────────────────────────

    private Projeto buscarProjeto(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Projeto não encontrado"));
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    private void validarSlugUnico(String titulo, Long idAtual) {
        if (projetoRepository.existsByTituloIgnoreCaseAndIdNot(
                titulo, idAtual != null ? idAtual : 0L)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Já existe um projeto com este título");
        }
    }

    private void validarPropriedade(Projeto projeto, Long solicitanteId, boolean isAdmin) {
        if (!isAdmin && !projeto.getCriador().getId().equals(solicitanteId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Sem permissão para modificar este projeto");
        }
    }
}
