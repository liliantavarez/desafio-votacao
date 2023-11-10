package com.db.api.stubs;

import com.db.api.dtos.request.PautaRequest;
import com.db.api.dtos.request.PautaRequestSessao;
import com.db.api.models.Pauta;

public interface PautaStub {
    static Pauta gerarPautaDtoValida() {
        return Pauta.builder()
                .titulo("Novas funcionalidades")
                .descricao("Discussão sobre a adição de notificações à aplicação.")
                .build();
    }

    static Pauta gerarPautaRequestDto() {
        return Pauta.builder()
                .id(1L)
                .titulo("Novas funcionalidades")
                .build();
    }

    static PautaRequest gerarPautaDtoTituloVazio() {
        return PautaRequest.builder()
                .titulo(" ")
                .descricao("Discussão sobre a adição de notificações à aplicação.")
                .build();
    }

    static PautaRequest gerarPautaRequest() {
        return PautaRequest.builder()
                .titulo("Novas funcionalidades")
                .descricao("Discussão sobre a adição de notificações à aplicação.")
                .build();
    }

    static PautaRequestSessao gerarPautaRequestSessao() {
        return PautaRequestSessao.builder()
                .titulo("Novas funcionalidades")
                .build();
    }

}
