package com.PontoCerto.controller;

import com.PontoCerto.dto.EscalaTrabalhoDTO;
import com.PontoCerto.models.EscalaTrabalho;
import com.PontoCerto.service.EscalaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/escala")
public class EscalaController {

    @Autowired
    private EscalaService escalaService;

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<String> cadastrarEscala(@RequestBody @Valid EscalaTrabalhoDTO dto) {
        escalaService.cadastrarOuAtualizarEscala(dto);
        return ResponseEntity.ok("Escala cadastrada com sucesso!");
    }

    @GetMapping("/funcionario")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<EscalaTrabalho> getEscala(@RequestParam String email) {
        EscalaTrabalho escala = escalaService.buscarEscalaPorUsuario(email);
        return ResponseEntity.ok(escala);
    }

    @DeleteMapping("/remover-folga")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<String> removerFolga(@RequestParam String email, @RequestParam String data) {
        escalaService.removerFolga(email, LocalDate.parse(data));
        return ResponseEntity.ok("Folga removida com sucesso!");
    }

    @PatchMapping("/adicionar-feriado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<String> adicionarFeriado(@RequestParam String email, @RequestParam String data) {
        escalaService.adicionarFeriado(email, LocalDate.parse(data));
        return ResponseEntity.ok("Feriado adicionado com sucesso!");
    }

    @DeleteMapping("/remover-feriado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<String> removerFeriado(@RequestParam String email, @RequestParam String data) {
        escalaService.removerFeriado(email, LocalDate.parse(data));
        return ResponseEntity.ok("Feriado removido com sucesso!");
    }
}