package com.PontoCerto.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class PontoResponseDTO {
    private Long id;
    private LocalDate data;
    private LocalTime entrada;
    private LocalTime inicioRefeicao;
    private LocalTime fimRefeicao;
    private LocalTime saida;
    private String nomeUsuario;
    private String horasTrabalhadas; // ✅ Campo para mostrar na resposta

    public PontoResponseDTO(Long id, LocalDate data, LocalTime entrada, LocalTime inicioRefeicao,
                            LocalTime fimRefeicao, LocalTime saida, String nomeUsuario, String horasTrabalhadas) {
        this.id = id;
        this.data = data;
        this.entrada = entrada;
        this.inicioRefeicao = inicioRefeicao;
        this.fimRefeicao = fimRefeicao;
        this.saida = saida;
        this.nomeUsuario = nomeUsuario;
        this.horasTrabalhadas = horasTrabalhadas;
    }

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalTime entrada) {
        this.entrada = entrada;
    }

    public LocalTime getInicioRefeicao() {
        return inicioRefeicao;
    }

    public void setInicioRefeicao(LocalTime inicioRefeicao) {
        this.inicioRefeicao = inicioRefeicao;
    }

    public LocalTime getFimRefeicao() {
        return fimRefeicao;
    }

    public void setFimRefeicao(LocalTime fimRefeicao) {
        this.fimRefeicao = fimRefeicao;
    }

    public LocalTime getSaida() {
        return saida;
    }

    public void setSaida(LocalTime saida) {
        this.saida = saida;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(String horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }
}
