package com.PontoCerto.dto;

import jakarta.validation.constraints.NotEmpty;

public class EmpresaRequest {
    @NotEmpty(message = "Nome da empresa é obrigatório")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
