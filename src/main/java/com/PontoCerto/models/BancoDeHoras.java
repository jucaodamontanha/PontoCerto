package com.PontoCerto.models;

import jakarta.persistence.*;

@Entity
public class BancoDeHoras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double saldoHoras; // pode ser positivo ou negativo

    @OneToOne
    private Usuario usuario;

    public BancoDeHoras() {
    }

    public BancoDeHoras(Long id, Double saldoHoras, Usuario usuario) {
        this.id = id;
        this.saldoHoras = saldoHoras;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSaldoHoras() {
        return saldoHoras;
    }

    public void setSaldoHoras(Double saldoHoras) {
        this.saldoHoras = saldoHoras;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
