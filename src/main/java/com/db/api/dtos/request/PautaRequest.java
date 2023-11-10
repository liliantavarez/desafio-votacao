package com.db.api.dtos.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class PautaRequest {
    @NotBlank(message = "Por favor, informe o titulo da pauta.")
    private String titulo;
    private String descricao;
}
