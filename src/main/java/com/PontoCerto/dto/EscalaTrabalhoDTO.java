package com.PontoCerto.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EscalaTrabalhoDTO {
    private String emailFuncionario;
    private LocalTime entrada;
    private LocalTime saida;
    private LocalTime inicioRefeicao;
    private LocalTime fimRefeicao;
    private List<DayOfWeek> diasTrabalho;
    private List<LocalDate> feriados;
    private List<LocalDate> folgasIndividuais;

    public String getEmailFuncionario() {
        return emailFuncionario;
    }

    public void setEmailFuncionario(String emailFuncionario) {
        this.emailFuncionario = emailFuncionario;
    }

    public LocalTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalTime entrada) {
        this.entrada = entrada;
    }

    public LocalTime getSaida() {
        return saida;
    }

    public void setSaida(LocalTime saida) {
        this.saida = saida;
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

    public List<DayOfWeek> getDiasTrabalho() {
        return diasTrabalho;
    }

    public void setDiasTrabalho(List<DayOfWeek> diasTrabalho) {
        this.diasTrabalho = diasTrabalho;
    }

    public List<LocalDate> getFeriados() {
        return feriados;
    }

    public void setFeriados(List<LocalDate> feriados) {
        this.feriados = feriados;
    }

    public List<LocalDate> getFolgasIndividuais() {
        return folgasIndividuais;
    }

    public void setFolgasIndividuais(List<LocalDate> folgasIndividuais) {
        this.folgasIndividuais = folgasIndividuais;
    }
}
