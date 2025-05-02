package com.PontoCerto.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class MarcarPontoFuncionarioDTO {
    @NotNull
    private String emailFuncionario;

    @NotNull
    private LocalDate data;

    private LocalTime entrada;
    private LocalTime inicioRefeicao;
    private LocalTime fimRefeicao;
    private LocalTime saida;
}
