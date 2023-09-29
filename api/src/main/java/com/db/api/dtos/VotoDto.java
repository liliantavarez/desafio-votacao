package com.db.api.dtos;

import com.db.api.dtos.request.AssociadoRequestVoto;
import com.db.api.enums.VotoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotoDto {
    @NotNull(message = "Por favor, informe o identificador da sessão que deseja votar.")
    private long sessao_id;
    @NotNull(message = "Por favor, informe o cpf.")
    private AssociadoRequestVoto associado;
    @Enumerated(EnumType.STRING)
    private VotoEnum votoEnum;
}