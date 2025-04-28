package com.PontoCerto.controller;

import com.PontoCerto.dto.EmpresaRequest;
import com.PontoCerto.models.Empresa;
import com.PontoCerto.repository.EmpresaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarEmpresa(@Valid @RequestBody EmpresaRequest request) {
        if (empresaRepository.findByNome(request.getNome()).isPresent()) {
            return ResponseEntity.badRequest().body("Empresa já cadastrada.");
        }

        Empresa empresa = new Empresa();
        empresa.setNome(request.getNome());
        empresaRepository.save(empresa);

        return ResponseEntity.ok("Empresa cadastrada com sucesso.");
    }
}
