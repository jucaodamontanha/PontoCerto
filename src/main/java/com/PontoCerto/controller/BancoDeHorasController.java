package com.PontoCerto.controller;

import com.PontoCerto.models.BancoDeHoras;
import com.PontoCerto.service.BancoDeHorasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/banco-de-horas")
public class BancoDeHorasController {

    @Autowired
    private BancoDeHorasService bancoDeHorasService;

    // ✅ 1. Consultar saldo de horas do usuário logado
    @GetMapping("/meu-saldo")
    public BancoDeHoras buscarMeuSaldo(Principal principal) {
        return bancoDeHorasService.buscarSaldoPorEmail(principal.getName());
    }

    // ✅ 2. Atualizar saldo de um usuário específico (uso restrito, como admin ou supervisor)
    @PutMapping("/atualizar/{usuarioId}")
    public BancoDeHoras atualizarSaldoManual(@PathVariable Long usuarioId, @RequestParam Double novoSaldo) {
        return bancoDeHorasService.atualizarSaldoManual(usuarioId, novoSaldo);
    }

    // ✅ 3. Listar saldo de todos da empresa do usuário logado (ex: para supervisores)
    @GetMapping("/empresa")
    public List<BancoDeHoras> listarSaldosPorEmpresa(Principal principal) {
        return bancoDeHorasService.listarSaldosDaEmpresa(principal.getName());
    }
}   