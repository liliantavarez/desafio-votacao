package com.db.api.dtos;

import com.db.api.dtos.request.PautaRequestSessao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessaoDto {
    private long id;
    private PautaRequestSessao pauta;
    private LocalDateTime dataEncerramento;
}
