package com.PontoCerto.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Ponto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private LocalTime entrada;

    private LocalTime inicioRefeicao;

    private LocalTime fimRefeicao;

    private LocalTime saida;

    @ManyToOne
    private Usuario usuario;

    public Ponto() {
    }

    public Ponto(Long id, LocalDate data, LocalTime entrada, LocalTime inicioRefeicao, LocalTime fimRefeicao, LocalTime saida, Usuario usuario) {
        this.id = id;
        this.data = data;
        this.entrada = entrada;
        this.inicioRefeicao = inicioRefeicao;
        this.fimRefeicao = fimRefeicao;
        this.saida = saida;
        this.usuario = usuario;
    }

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
