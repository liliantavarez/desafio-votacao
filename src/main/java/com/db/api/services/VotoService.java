package com.db.api.services;

import com.db.api.dtos.VotoDto;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.exceptions.VotoDuplicadoException;
import com.db.api.models.Associado;
import com.db.api.models.Sessao;
import com.db.api.models.Voto;
import com.db.api.repositories.VotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@AllArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final AssociadoService associadoService;
    private final SessaoService sessaoService;

    @Transactional
    public void registrarVoto(@Valid VotoDto votoDto) {
        associadoJaVotou(votoDto);

        Sessao sessao = sessaoService.validarSessao(votoDto.getSessao_id());
        Associado associado = associadoService.validarAssociadoParaVotacao(votoDto.getAssociado().getCpf());

        Voto voto = new Voto(sessao, associado, votoDto.getVotoEnum());
        votoRepository.save(voto);

    }

    private void associadoJaVotou(VotoDto votoDto) {
        boolean associadoJaVotou = votoRepository.existsVotoBySessaoIdAndAssociadoCpf(votoDto.getSessao_id(), votoDto.getAssociado().getCpf());

        if (associadoJaVotou) {
            throw new VotoDuplicadoException();
        }
    }

}
