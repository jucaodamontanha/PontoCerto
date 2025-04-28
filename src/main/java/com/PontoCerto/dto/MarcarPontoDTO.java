package com.PontoCerto.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class MarcarPontoDTO {
    @NotNull(message = "A data é obrigatória")
    private LocalDate data;
    @NotNull(message = "O horário de entrada é obrigatório")
    private LocalTime entrada;
    private LocalTime inicioRefeicao;
    private LocalTime fimRefeicao;
    @NotNull(message = "O horário de saída é obrigatório")
    private LocalTime saida;

    public MarcarPontoDTO() {
    }

    public MarcarPontoDTO(LocalDate data, LocalTime entrada, LocalTime inicioRefeicao, LocalTime fimRefeicao, LocalTime saida) {
        this.data = data;
        this.entrada = entrada;
        this.inicioRefeicao = inicioRefeicao;
        this.fimRefeicao = fimRefeicao;
        this.saida = saida;
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
}
