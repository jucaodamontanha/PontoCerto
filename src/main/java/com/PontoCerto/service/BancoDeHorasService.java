package com.PontoCerto.service;

import com.PontoCerto.models.BancoDeHoras;
import com.PontoCerto.models.Usuario;
import com.PontoCerto.repository.BancoDeHorasRepository;
import com.PontoCerto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BancoDeHorasService {

    @Autowired
    private BancoDeHorasRepository bancoDeHorasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ✅ Buscar saldo de horas do usuário autenticado
    public BancoDeHoras buscarSaldoPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return bancoDeHorasRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Banco de horas não encontrado para o usuário"));
    }

    // ✅ Atualizar manualmente o saldo (uso restrito)
    public BancoDeHoras atualizarSaldoManual(Long usuarioId, Double novoSaldo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        BancoDeHoras banco = bancoDeHorasRepository.findByUsuario(usuario)
                .orElse(new BancoDeHoras(null, 0.0, usuario)); // cria novo se não existir

        banco.setSaldoHoras(novoSaldo);
        return bancoDeHorasRepository.save(banco);
    }

    // ✅ Listar todos os saldos da empresa (supervisor/admin)
    public List<BancoDeHoras> listarSaldosDaEmpresa(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Long empresaId = usuario.getEmpresa().getId();

        return bancoDeHorasRepository.findAllByUsuario_Empresa_Id(empresaId);
    }

    // ✅ Atualizar saldo automaticamente após marcação de ponto
    public void atualizarSaldoAutomatico(Usuario usuario, double horasTrabalhadas) {
        BancoDeHoras banco = bancoDeHorasRepository.findByUsuario(usuario)
                .orElse(new BancoDeHoras(null, 0.0, usuario)); // cria novo se não existir

        double saldoAtual = banco.getSaldoHoras() != null ? banco.getSaldoHoras() : 0.0;
        banco.setSaldoHoras(saldoAtual + horasTrabalhadas);

        bancoDeHorasRepository.save(banco);
    }
}
