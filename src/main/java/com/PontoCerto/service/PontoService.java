package com.PontoCerto.service;

import com.PontoCerto.dto.MarcarPontoDTO;
import com.PontoCerto.dto.PontoResponseDTO;
import com.PontoCerto.models.EscalaTrabalho;
import com.PontoCerto.models.Ponto;
import com.PontoCerto.models.Usuario;
import com.PontoCerto.repository.PontoRepository;
import com.PontoCerto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class PontoService {

    @Autowired
    private PontoRepository pontoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EscalaService escalaService;

    public void marcarPonto(MarcarPontoDTO dto, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        EscalaTrabalho escala = escalaService.buscarEscalaPorUsuario(emailUsuario);

        // Validação de dia permitido
        if (!escalaService.podeTrabalharNesteDia(emailUsuario, dto.getData())) {
            throw new RuntimeException("Não é permitido registrar ponto nesta data (folga, feriado ou dia não previsto na escala)");
        }

        // Validação de horários (se preenchidos)
        if (dto.getEntrada() != null && dto.getEntrada().isBefore(escala.getEntrada().minusMinutes(30))) {
            throw new RuntimeException("Horário de entrada muito adiantado em relação à escala");
        }
        if (dto.getSaida() != null && dto.getSaida().isAfter(escala.getSaida().plusMinutes(30))) {
            throw new RuntimeException("Horário de saída muito tardio em relação à escala");
        }
        if (dto.getInicioRefeicao() != null && dto.getInicioRefeicao().isBefore(escala.getInicioRefeicao().minusMinutes(30))) {
            throw new RuntimeException("Início do intervalo de refeição muito adiantado em relação à escala");
        }
        if (dto.getFimRefeicao() != null && dto.getFimRefeicao().isAfter(escala.getFimRefeicao().plusMinutes(30))) {
            throw new RuntimeException("Término do intervalo de refeição muito tardio em relação à escala");
        }

        // Persistência do ponto
        Ponto ponto = new Ponto();
        ponto.setData(dto.getData());
        ponto.setEntrada(dto.getEntrada());
        ponto.setInicioRefeicao(dto.getInicioRefeicao());
        ponto.setFimRefeicao(dto.getFimRefeicao());
        ponto.setSaida(dto.getSaida());
        ponto.setUsuario(usuario);

        pontoRepository.save(ponto);
    }


    public List<Ponto> listarPontosPorUsuario(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return pontoRepository.findByUsuario(usuario);
    }

    public Ponto buscarPontoPorData(String emailUsuario, LocalDate data) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return pontoRepository.findByUsuarioAndData(usuario, data)
                .orElseThrow(() -> new RuntimeException("Ponto não encontrado para essa data"));
    }

    public Ponto atualizarPonto(Long id, MarcarPontoDTO dto) {
        Ponto ponto = pontoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ponto não encontrado"));

        ponto.setEntrada(dto.getEntrada());
        ponto.setInicioRefeicao(dto.getInicioRefeicao());
        ponto.setFimRefeicao(dto.getFimRefeicao());
        ponto.setSaida(dto.getSaida());

        return pontoRepository.save(ponto);
    }

    public void deletarPonto(Long id) {
        if (!pontoRepository.existsById(id)) {
            throw new RuntimeException("Ponto não encontrado");
        }
        pontoRepository.deleteById(id);
    }

    public List<PontoResponseDTO> listarPontosPorEmpresa(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Long empresaId = usuario.getEmpresa().getId();

        List<Ponto> pontos = pontoRepository.findAllByUsuario_Empresa_Id(empresaId);

        return pontos.stream().map(ponto -> {
            String horasTrabalhadas = calcularHorasTrabalhadas(ponto);
            return new PontoResponseDTO(
                    ponto.getId(),
                    ponto.getData(),
                    ponto.getEntrada(),
                    ponto.getInicioRefeicao(),
                    ponto.getFimRefeicao(),
                    ponto.getSaida(),
                    ponto.getUsuario().getNome(),
                    horasTrabalhadas
            );
        }).toList();
    }

    // Método para calcular as horas trabalhadas
    private String calcularHorasTrabalhadas(Ponto ponto) {
        if (ponto.getEntrada() != null && ponto.getSaida() != null) {
            Duration totalExpediente = Duration.between(ponto.getEntrada(), ponto.getSaida());

            Duration tempoRefeicao = Duration.ZERO;
            if (ponto.getInicioRefeicao() != null && ponto.getFimRefeicao() != null) {
                tempoRefeicao = Duration.between(ponto.getInicioRefeicao(), ponto.getFimRefeicao());
            }

            Duration horasTrabalhadas = totalExpediente.minus(tempoRefeicao);

            long horas = horasTrabalhadas.toHours();
            long minutos = horasTrabalhadas.toMinutesPart();

            return String.format("%02d:%02d", horas, minutos);
        } else {
            return "00:00"; // Se não tiver entrada ou saída registrada
        }
    }
    public void marcarPontoParaFuncionario(MarcarPontoDTO dto, String emailFuncionario) {
        Usuario funcionario = usuarioRepository.findByEmail(emailFuncionario)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Ponto ponto = new Ponto();
        ponto.setData(dto.getData());
        ponto.setEntrada(dto.getEntrada());
        ponto.setInicioRefeicao(dto.getInicioRefeicao());
        ponto.setFimRefeicao(dto.getFimRefeicao());
        ponto.setSaida(dto.getSaida());
        ponto.setUsuario(funcionario);

        pontoRepository.save(ponto);
    }

}
