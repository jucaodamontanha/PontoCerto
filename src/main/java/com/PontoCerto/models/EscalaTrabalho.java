package com.PontoCerto.models;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class EscalaTrabalho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Usuario funcionario;

    private LocalTime entrada;
    private LocalTime saida;
    private LocalTime inicioRefeicao;
    private LocalTime fimRefeicao;

    @ElementCollection
    private List<DayOfWeek> diasTrabalho;

    @ElementCollection
    private List<LocalDate> feriados;

    @ElementCollection
    private List<LocalDate> folgasIndividuais;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Usuario funcionario) {
        this.funcionario = funcionario;
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