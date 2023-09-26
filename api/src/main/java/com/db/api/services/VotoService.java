package com.db.api.services;

import com.db.api.dtos.VotoDto;
import com.db.api.enums.StatusCPF;
import com.db.api.enums.StatusSessao;
import com.db.api.exceptions.*;
import com.db.api.models.Associado;
import com.db.api.models.Sessao;
import com.db.api.models.Voto;
import com.db.api.repositories.AssociadoRepository;
import com.db.api.repositories.SessaoRepository;
import com.db.api.repositories.VotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final AssociadoRepository associadoRepository;
    private final SessaoRepository sessaoRepository;
    private final Validator validator;

    @Transactional
    public void registrarVoto(VotoDto votoDto) {
        validarParametros(votoDto);

        Sessao sessao = validarSessao(votoDto.getSessao_id());
        Associado associado = validarAssociado(votoDto.getAssociado().getCpf());

        Voto voto = new Voto(sessao, associado, votoDto.getVotoEnum());
        votoRepository.save(voto);

    }

    @Transactional
    public Associado validarAssociado(String cpfAssociado) {
        Associado associado = associadoRepository.findByCpf(cpfAssociado).orElseThrow(() -> new RegistroNaoEncontradoException("Associado não encontrada."));

        if (associado.getStatusCPF() == StatusCPF.UNABLE_TO_VOTE) {
            throw new NaoPodeVotarException();
        }

        desabilitarCpf(associado);

        return associado;
    }

    @Transactional
    public Sessao validarSessao(long sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Sessão não encontrada."));

        if (sessao.getDataEncerramento().isBefore(LocalDateTime.now()) && sessao.getStatusSessao() == StatusSessao.ABERTA) {
            encerrarSessao(sessao);
        }

        if (sessao.getStatusSessao() == StatusSessao.ENCERRADA) {
            throw new SessaoEncerradaException();
        }

        return sessao;
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

    private void encerrarSessao(Sessao sessao) {
        sessao.setStatusSessao(StatusSessao.ENCERRADA);
        sessaoRepository.save(sessao);
    }

    private void desabilitarCpf(Associado associado) {
        associado.setStatusCPF(StatusCPF.UNABLE_TO_VOTE);
        associadoRepository.save(associado);
    }

}
