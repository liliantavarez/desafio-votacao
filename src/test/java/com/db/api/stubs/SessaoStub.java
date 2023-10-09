package com.db.api.stubs;

import com.db.api.enums.StatusSessao;
import com.db.api.models.Pauta;
import com.db.api.models.Sessao;

import java.time.LocalDateTime;

public interface SessaoStub {
    static Sessao gerarSessaoDtoValida() {
        return Sessao.builder()
                .pauta(PautaStub.gerarPautaRequestDto())
                .dataEncerramento(LocalDateTime.now().plusMinutes(60))
                .build();
    }

    static Sessao gerarSessaoAberta() {
        return Sessao.builder()
                .id(1L)
                .pauta(PautaStub.gerarPautaRequestDto())
                .dataEncerramento(LocalDateTime.now().plusMinutes(60))
                .statusSessao(StatusSessao.ABERTA)
                .build();
    }
    static Sessao gerarSessaoEncerrada() {
        return Sessao.builder()
                .id(2L)
                .pauta(PautaStub.gerarPautaRequestDto())
                .dataEncerramento(LocalDateTime.now())
                .statusSessao(StatusSessao.ENCERRADA)
                .build();
    }
    static Sessao gerarSessaoDtoDataEncerramentoInvalida() {
        return Sessao.builder()
                .pauta(PautaStub.gerarPautaDtoValida())
                .dataEncerramento(LocalDateTime.parse("2023-09-10T12:00:00"))
                .build();
    }

}
