package com.PontoCerto.dto;

import com.PontoCerto.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UsuarioRequest {

    @NotEmpty(message = "Nome é obrigatório")
    private String nome;

    @NotEmpty(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotEmpty(message = "Senha é obrigatória")
    private String senha;
    @NotEmpty(message = "Role é obrigatório")
    private String role;

    @NotEmpty(message = "Nome da empresa é obrigatório")
    private String nomeEmpresa; // Campo para associar o usuário a uma empresa

    // Construtor completo (não é obrigatório, mas útil para facilitar a criação de objetos)
    public UsuarioRequest(String nome, String email, String senha, String role, String nomeEmpresa) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.nomeEmpresa = nomeEmpresa;
    }

    // Construtor vazio (necessário para o Spring usar a injeção de dependências)
    public UsuarioRequest() {
    }

    // Getters e setters

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Role getRole() {
        return Role.valueOf(role.toUpperCase()); // Converte String para Enum
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
