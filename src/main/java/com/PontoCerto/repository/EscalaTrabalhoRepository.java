package com.PontoCerto.repository;

import com.PontoCerto.models.EscalaTrabalho;
import com.PontoCerto.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EscalaTrabalhoRepository extends JpaRepository<EscalaTrabalho, Long> {
    Optional<EscalaTrabalho> findByFuncionario(Usuario usuario);
}