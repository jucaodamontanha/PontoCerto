package com.PontoCerto.repository;

import com.PontoCerto.models.Empresa;
import com.PontoCerto.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTokenResetSenha(String token);
    List<Usuario> findByEmpresa_Id(Long empresaId);
    List<Usuario> findByEmpresa(Empresa empresa);

}