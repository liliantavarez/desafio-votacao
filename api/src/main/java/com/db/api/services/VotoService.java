package com.db.api.services;

import com.db.api.dtos.VotoDto;
import com.db.api.enums.StatusCPF;
import com.db.api.exceptions.NaoPodeVotarException;
import com.db.api.exceptions.ParametrosInvalidosException;
import com.db.api.exceptions.VotoDuplicadoException;
import com.db.api.models.Associado;
import com.db.api.models.Sessao;
import com.db.api.models.Voto;
import com.db.api.repositories.VotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Service
@AllArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final AssociadoService associadoService;
    private final SessaoService sessaoService;
    private final Validator validator;

    @Transactional
    public void registrarVoto(VotoDto votoDto) {
        validarParametros(votoDto);

        Sessao sessao = sessaoService.validarSessao(votoDto.getSessao_id());
        Associado associado = associadoService.validarAssociado(votoDto.getAssociado().getCpf());

        Voto voto = new Voto(sessao, associado, votoDto.getVotoEnum());
        votoRepository.save(voto);

    }


    private void validarParametros(VotoDto votoDto) {
        Set<ConstraintViolation<VotoDto>> violations = validator.validate(votoDto);

        if (!violations.isEmpty()) {
            throw new ParametrosInvalidosException("Por favor, revise os dados!");
        }

        boolean associadoJaVotou = votoRepository.existsVotoBySessaoIdAndAssociadoCpf(votoDto.getSessao_id(), votoDto.getAssociado().getCpf());

        if (associadoJaVotou) {
            throw new VotoDuplicadoException();
        }
    }


}
