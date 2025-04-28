package com.PontoCerto.dto;

public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
    private String role;
    private String nomeEmpresa;

    public UsuarioResponse() {}

    public UsuarioResponse(Long id, String nome, String email, String role, String nomeEmpresa) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.nomeEmpresa = nomeEmpresa;
    }

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }
}
