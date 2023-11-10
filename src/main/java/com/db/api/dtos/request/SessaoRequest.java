package com.db.api.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SessaoRequest {
    private PautaRequestSessao pauta;
    @Future
    private LocalDateTime dataEncerramento;
}
