package com.db.api.dtos.response;

import com.db.api.dtos.PautaDto;
import com.db.api.enums.ResultadoSessao;
import com.db.api.enums.StatusSessao;
import com.db.api.models.Pauta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessaoResponse {
    private Pauta pauta;
    private LocalDateTime dataEncerramento;
    private ResultadoSessao resultadoSessao;
}
