package com.db.api.dtos.response;

import com.db.api.enums.ResultadoSessao;
import com.db.api.models.Pauta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessaoResponse {
    private Pauta pauta;
    private LocalDateTime dataEncerramento;
    private ResultadoSessao resultadoSessao;
}
