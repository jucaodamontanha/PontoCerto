package com.PontoCerto.repository;


import com.PontoCerto.models.NotificacaoResponsavel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoResponsavelRepository extends JpaRepository<NotificacaoResponsavel, Long> {
    List<NotificacaoResponsavel> findAllByAtivoTrue();
}