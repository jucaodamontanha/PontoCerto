package com.PontoCerto.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    FUNCIONARIO,
    GESTOR,
    ADMIN;


    @Override
    public String getAuthority() {
        return name();
    }
}
