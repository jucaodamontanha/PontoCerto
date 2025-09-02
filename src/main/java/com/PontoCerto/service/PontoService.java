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
import java.time.LocalTime;
import java.util.List;

@Service
public class PontoService {

    @Autowired
    private BancoDeHorasService bancoDeHorasService;


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

        boolean foraDaEscala = false;
        StringBuilder motivo = new StringBuilder();

        // Validação de dia permitido
        if (!escalaService.podeTrabalharNesteDia(emailUsuario, dto.getData())) {
            foraDaEscala = true;
            motivo.append("Registro em dia não previsto na escala. ");
        }

        if (dto.getEntrada() != null && dto.getEntrada().isBefore(escala.getEntrada().minusMinutes(30))) {
            foraDaEscala = true;
            motivo.append("Entrada muito adiantada. ");
        }
        if (dto.getSaida() != null && dto.getSaida().isAfter(escala.getSaida().plusMinutes(30))) {
            foraDaEscala = true;
            motivo.append("Saída muito tardia. ");
        }
        if (dto.getInicioRefeicao() != null && dto.getInicioRefeicao().isBefore(escala.getInicioRefeicao().minusMinutes(30))) {
            foraDaEscala = true;
            motivo.append("Início da refeição muito adiantado. ");
        }
        if (dto.getFimRefeicao() != null && dto.getFimRefeicao().isAfter(escala.getFimRefeicao().plusMinutes(30))) {
            foraDaEscala = true;
            motivo.append("Fim da refeição muito tardio. ");
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

        // ✅ Atualiza o banco de horas APENAS se tudo estiver preenchido
        if (!foraDaEscala &&
                dto.getEntrada() != null && dto.getInicioRefeicao() != null &&
                dto.getFimRefeicao() != null && dto.getSaida() != null) {

            double horasTrabalhadas = calcularHorasTrabalhadas(
                    dto.getEntrada(),
                    dto.getInicioRefeicao(),
                    dto.getFimRefeicao(),
                    dto.getSaida()
            );

            // Subtrai a carga horária da escala para calcular o saldo (positiva ou negativa)
            double cargaHorariaEscala = Duration.between(escala.getEntrada(), escala.getSaida())
                    .minus(Duration.between(escala.getInicioRefeicao(), escala.getFimRefeicao()))
                    .toMinutes() / 60.0;

            double saldoDoDia = horasTrabalhadas - cargaHorariaEscala;

            bancoDeHorasService.atualizarSaldoAutomatico(usuario, saldoDoDia);
        }
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
    private double calcularHorasTrabalhadas(LocalTime entrada, LocalTime inicioRefeicao,
                                            LocalTime fimRefeicao, LocalTime saida) {
        Duration tempoTotal = Duration.between(entrada, saida);
        Duration tempoRefeicao = Duration.between(inicioRefeicao, fimRefeicao);
        Duration tempoUtil = tempoTotal.minus(tempoRefeicao);

        return tempoUtil.toMinutes() / 60.0; // retorna em horas (ex: 7.5h)
    }


}
