package com.PontoCerto.service;

import com.PontoCerto.dto.EscalaTrabalhoDTO;
import com.PontoCerto.models.EscalaTrabalho;
import com.PontoCerto.models.Usuario;
import com.PontoCerto.repository.EscalaTrabalhoRepository;
import com.PontoCerto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EscalaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EscalaTrabalhoRepository escalaRepository;

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

    public void removerFolga(String email, LocalDate data) {
        EscalaTrabalho escala = buscarEscalaPorUsuario(email);
        escala.getFolgasIndividuais().remove(data);
        escalaRepository.save(escala);
    }

    public void adicionarFeriado(String email, LocalDate data) {
        EscalaTrabalho escala = buscarEscalaPorUsuario(email);
        if (!escala.getFeriados().contains(data)) {
            escala.getFeriados().add(data);
            escalaRepository.save(escala);
        }
    }
    public void removerFeriado(String email, LocalDate data) {
        EscalaTrabalho escala = buscarEscalaPorUsuario(email);
        escala.getFeriados().remove(data);
        escalaRepository.save(escala);
    }

}