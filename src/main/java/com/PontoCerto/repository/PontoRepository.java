package com.PontoCerto.repository;

import com.PontoCerto.models.Ponto;
import com.PontoCerto.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PontoRepository extends JpaRepository<Ponto, Long> {
    List<Ponto> findByUsuario(Usuario usuario);
    Optional<Ponto> findByUsuarioAndData(Usuario usuario, LocalDate data);
    List<Ponto> findAllByUsuario_Empresa_Id(Long empresaId);
}