package com.PontoCerto.service;

import com.PontoCerto.dto.MarcarPontoDTO;
import com.PontoCerto.dto.PontoResponseDTO;
import com.PontoCerto.models.Ponto;
import com.PontoCerto.models.Usuario;
import com.PontoCerto.repository.PontoRepository;
import com.PontoCerto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PontoService {

    @Autowired
    private  PontoRepository pontoRepository;
    @Autowired
    private  UsuarioRepository usuarioRepository;


    public void marcarPonto(MarcarPontoDTO dto, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Ponto ponto = new Ponto();
        ponto.setData(dto.getData());
        ponto.setEntrada(dto.getEntrada());
        ponto.setInicioRefeicao(dto.getInicioRefeicao());
        ponto.setFimRefeicao(dto.getFimRefeicao());
        ponto.setSaida(dto.getSaida());
        ponto.setUsuario(usuario);

        pontoRepository.save(ponto);
    }
    // Buscar todos os pontos de um usuário
    public List<Ponto> listarPontosPorUsuario(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return pontoRepository.findByUsuario(usuario);
    }

    // Buscar ponto de um usuário por data
    public Ponto buscarPontoPorData(String emailUsuario, LocalDate data) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return pontoRepository.findByUsuarioAndData(usuario, data)
                .orElseThrow(() -> new RuntimeException("Ponto não encontrado para essa data"));
    }

    // Atualizar ponto existente
    public Ponto atualizarPonto(Long id, MarcarPontoDTO dto) {
        Ponto ponto = pontoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ponto não encontrado"));

        ponto.setEntrada(dto.getEntrada());
        ponto.setInicioRefeicao(dto.getInicioRefeicao());
        ponto.setFimRefeicao(dto.getFimRefeicao());
        ponto.setSaida(dto.getSaida());

        return pontoRepository.save(ponto);
    }

    // Deletar ponto por ID
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

        return pontos.stream().map(ponto ->
                new PontoResponseDTO(
                        ponto.getId(),
                        ponto.getData(),
                        ponto.getEntrada(),
                        ponto.getInicioRefeicao(),
                        ponto.getFimRefeicao(),
                        ponto.getSaida(),
                        ponto.getUsuario().getNome()
                )
        ).toList();
    }

}
