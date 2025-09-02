package com.PontoCerto.service;

import com.PontoCerto.dto.EscalaTrabalhoDTO;
import com.PontoCerto.models.EscalaTrabalho;
import com.PontoCerto.models.Usuario;
import com.PontoCerto.repository.EscalaTrabalhoRepository;
import com.PontoCerto.repository.NotificacaoResponsavelRepository;
import com.PontoCerto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EscalaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EscalaTrabalhoRepository escalaRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private NotificacaoResponsavelRepository notificacaoResponsavelRepository;
    @Autowired
    private EmailService emailService;


    public void cadastrarOuAtualizarEscala(EscalaTrabalhoDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmailFuncionario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        EscalaTrabalho escala = escalaRepository.findByFuncionario(usuario).orElse(new EscalaTrabalho());
        escala.setFuncionario(usuario);
        escala.setEntrada(dto.getEntrada());
        escala.setSaida(dto.getSaida());
        escala.setInicioRefeicao(dto.getInicioRefeicao());
        escala.setFimRefeicao(dto.getFimRefeicao());
        escala.setDiasTrabalho(dto.getDiasTrabalho());
        escala.setFeriados(dto.getFeriados());
        escala.setFolgasIndividuais(dto.getFolgasIndividuais());

        escalaRepository.save(escala);
    }

    public EscalaTrabalho buscarEscalaPorUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return escalaRepository.findByFuncionario(usuario)
                .orElseThrow(() -> new RuntimeException("Escala de trabalho não encontrada"));
    }

    public boolean podeTrabalharNesteDia(String email, LocalDate data) {
        EscalaTrabalho escala = buscarEscalaPorUsuario(email);
        return escala.getDiasTrabalho().contains(data.getDayOfWeek())
                && !escala.getFeriados().contains(data)
                && !escala.getFolgasIndividuais().contains(data);
    }

    public boolean estaForaDaEscala(String email, LocalDate data) {
        return !podeTrabalharNesteDia(email, data);
    }

    public void notificarGestores(String emailFuncionario, LocalDate data) {
        List<String> emails = notificacaoResponsavelRepository.findAllByAtivoTrue()
                .stream()
                .map(n -> n.getEmail())
                .toList();

        if (emails.isEmpty()) return;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emails.toArray(new String[0]));
        message.setSubject("⚠️ Alerta de marcação fora da escala");
        message.setText("Funcionário " + emailFuncionario + " marcou ponto fora da escala no dia " + data + ".");
        mailSender.send(message);
    }

    public void verificarEscalaEAvisar(String emailFuncionario, LocalDate data) {
        if (estaForaDaEscala(emailFuncionario, data)) {
            notificarGestores(emailFuncionario, data);
        }
    }

    public void removerFolga(String email, LocalDate data) {
        EscalaTrabalho escala = buscarEscalaPorUsuario(email);
        List<LocalDate> folgas = escala.getFolgasIndividuais();
        folgas.remove(data);
        escala.setFolgasIndividuais(folgas);
        escalaRepository.save(escala);
    }

    public void adicionarFeriado(String email, LocalDate data) {
        EscalaTrabalho escala = buscarEscalaPorUsuario(email);
        List<LocalDate> feriados = escala.getFeriados();
        if (!feriados.contains(data)) {
            feriados.add(data);
            escala.setFeriados(feriados);
            escalaRepository.save(escala);
        }
    }

    public void removerFeriado(String email, LocalDate data) {
        EscalaTrabalho escala = buscarEscalaPorUsuario(email);
        List<LocalDate> feriados = escala.getFeriados();
        feriados.remove(data);
        escala.setFeriados(feriados);
        escalaRepository.save(escala);
    }
    private void notificarForaDaEscala(String nomeUsuario, LocalDate data, String motivo) {
        List<String> emails = notificacaoResponsavelRepository.findAllByAtivoTrue()
                .stream()
                .map(n -> n.getEmail())
                .toList();

        String assunto = "Alerta de marcação fora da escala";
        String mensagem = String.format("O usuário %s registrou ponto fora da escala no dia %s.\nMotivo: %s",
                nomeUsuario, data, motivo);

        emailService.enviarEmail(emails, assunto, mensagem);
    }
}