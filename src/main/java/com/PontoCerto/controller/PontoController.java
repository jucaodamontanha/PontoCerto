package com.PontoCerto.controller;

import com.PontoCerto.dto.MarcarPontoDTO;
import com.PontoCerto.dto.PontoResponseDTO;
import com.PontoCerto.models.Ponto;
import com.PontoCerto.service.PontoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ponto")
public class PontoController {
    @Autowired
    private  PontoService pontoService;

    @PostMapping("/marcar")
    public ResponseEntity<String> marcarPonto(@Valid @RequestBody MarcarPontoDTO dto,
                                              Principal principal) {
        pontoService.marcarPonto(dto, principal.getName());
        return ResponseEntity.ok("Ponto registrado com sucesso!");
    }
    // Listar pontos do usuário logado
    @GetMapping("/meus")
    public ResponseEntity<List<Ponto>> listarMeusPontos(Principal principal) {
        List<Ponto> pontos = pontoService.listarPontosPorUsuario(principal.getName());
        return ResponseEntity.ok(pontos);
    }

    // Buscar ponto por data (ex: /ponto/por-data?data=2025-04-20)
    @GetMapping("/por-data")
    public ResponseEntity<Ponto> buscarPontoPorData(@RequestParam("data") String data,
                                                    Principal principal) {
        Ponto ponto = pontoService.buscarPontoPorData(principal.getName(), LocalDate.parse(data));
        return ResponseEntity.ok(ponto);
    }

    // Atualizar ponto
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Ponto> atualizarPonto(@Valid @PathVariable Long id,
                                                @RequestBody MarcarPontoDTO dto) {
        Ponto pontoAtualizado = pontoService.atualizarPonto(id, dto);
        return ResponseEntity.ok(pontoAtualizado);
    }

    // Deletar ponto
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarPonto(@PathVariable Long id) {
        pontoService.deletarPonto(id);
        return ResponseEntity.ok("Ponto deletado com sucesso!");
    }

    @GetMapping("/todos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<List<PontoResponseDTO>> listarTodosPontos(Principal principal) {
        List<PontoResponseDTO> pontos = pontoService.listarPontosPorEmpresa(principal.getName());
        return ResponseEntity.ok(pontos);
    }
    @PostMapping("/admin/marcar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<String> marcarPontoParaFuncionario(@Valid @RequestBody MarcarPontoDTO dto,
                                                             @RequestParam String emailFuncionario) {
        pontoService.marcarPontoParaFuncionario(dto, emailFuncionario);
        return ResponseEntity.ok("Ponto lançado com sucesso!");
    }

}