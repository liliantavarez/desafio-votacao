package com.db.api.stubs;

import com.db.api.dtos.request.AssociadoRequest;
import com.db.api.dtos.request.AssociadoRequestVoto;
import com.db.api.models.Associado;

public interface AssociadoStub {
    static Associado gerarAssociadoDtoValida() {
        return Associado.builder()
                .id(1L)
                .nome("Carla Silva")
                .cpf("44271476072")
                .build();
    }

    static AssociadoRequest gerarAssociadoRequest() {
        return AssociadoRequest.builder()
                .nome("Carla Silva")
                .cpf("44271476072")
                .build();
    }

    static AssociadoRequestVoto gerarAssociadoRequestValida() {
        return AssociadoRequestVoto.builder()
                .cpf("44271476072")
                .build();
    }

    static AssociadoRequestVoto gerarAssociadoNaoCadastrado() {
        return AssociadoRequestVoto.builder()
                .cpf("51931799830")
                .build();
    }

    static Associado gerarAssociadoDtoCpfInvalida() {
        return Associado.builder()
                .nome("Mario Souza")
                .cpf("101010")
                .build();
    }

    static Associado gerarAssociadoDtoNomeVazio() {
        return Associado.builder()
                .nome("")
                .cpf("62912673577")
                .build();
    }
}
