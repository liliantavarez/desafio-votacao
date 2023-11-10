package com.db.api.dtos.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
public class PautaRequestSessao {
    @NotBlank(message = "Por favor, informe o titulo da pauta.")
    private String titulo;
    @JsonCreator
    public PautaRequestSessao(@JsonProperty("titulo") String titulo) {
        this.titulo = titulo;
    }
}
