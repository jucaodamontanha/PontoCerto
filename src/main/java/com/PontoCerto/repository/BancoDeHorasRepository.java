package com.PontoCerto.repository;

import com.PontoCerto.models.BancoDeHoras;
import com.PontoCerto.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BancoDeHorasRepository extends JpaRepository<BancoDeHoras, Long> {
    Optional<BancoDeHoras> findByUsuario(Usuario usuario);
    List<BancoDeHoras> findAllByUsuario_Empresa_Id(Long empresaId);
}
