package com.db.api.stubs;

import com.db.api.dtos.VotoDto;
import com.db.api.enums.VotoEnum;
import com.db.api.models.Associado;
import com.db.api.models.Voto;

public interface VotoStub {
    static Voto gerarVotoValido() {
        return Voto.builder()
                .id(1L)
                .associado(AssociadoStub.gerarAssociadoDtoValida())
                .sessao(SessaoStub.gerarSessaoAberta())
                .votoEnum(VotoEnum.SIM)
                .build();
    }

    static VotoDto gerarVotoDtoValida() {
        return VotoDto.builder()
                .associado(AssociadoStub.gerarAssociadoRequestValida())
                .sessao_id(SessaoStub.gerarSessaoAberta().getId())
                .votoEnum(VotoEnum.SIM)
                .build();
    }

    static VotoDto gerarVotoSessaoInvalida() {
        return VotoDto.builder()
                .associado(AssociadoStub.gerarAssociadoRequestValida())
                .sessao_id(2L)
                .votoEnum(VotoEnum.SIM)
                .build();
    }
    static VotoDto gerarVotoSessaoEncerrada() {
        return VotoDto.builder()
                .associado(AssociadoStub.gerarAssociadoRequestValida())
                .sessao_id(SessaoStub.gerarSessaoEncerrada().getId())
                .votoEnum(VotoEnum.SIM)
                .build();
    }

    static VotoDto gerarVotoAssociadoInvalida() {
        return VotoDto.builder()
                .associado(AssociadoStub.gerarAssociadoNaoCadastrado())
                .sessao_id(1L)
                .votoEnum(VotoEnum.SIM)
                .build();
    }

}
