package com.db.api.services;

import com.db.api.client.response.ConsultaCPFResponse;
import com.db.api.dtos.AssociadoDto;
import com.db.api.enums.StatusCPF;
import com.db.api.exceptions.AssociadoJaCadastradoException;
import com.db.api.exceptions.NaoPodeVotarException;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.models.Associado;
import com.db.api.repositories.AssociadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AssociadoService {

    private final AssociadoRepository associadoRepository;
    private final ConsultaCPFService consultaCPFService;

    public void registrarAssociado(@Valid AssociadoDto associadoDto) {
        validarParametros(associadoDto);

        Associado associado = new Associado(associadoDto.getNome(), associadoDto.getCpf());
        associadoRepository.save(associado);
    }

    private void validarParametros(AssociadoDto associadoDto) {
        if (associadoCadastrado(associadoDto.getCpf())) {
            throw new AssociadoJaCadastradoException();
        }
    }

    private boolean associadoCadastrado(String cpfAssociado) {
        return associadoRepository.existsAssociadoByCpf(cpfAssociado);
    }

    public Associado buscarAssociadoPorCPF(String cpfAssociado) {
        return associadoRepository.findByCpf(cpfAssociado).orElseThrow(() -> new RegistroNaoEncontradoException("Associado não encontrada."));
    }

    public Associado validarAssociado(String cpfAssociado) {
        Associado associado = buscarAssociadoPorCPF(cpfAssociado);
        ConsultaCPFResponse consultaCPF = consultaCPFService.verificarSituacaoCPF(associado.getCpf());

        if (!Objects.equals(consultaCPF.getSituacao(), "Regular")) {
            associado.setStatusCPF(StatusCPF.NAO_PODE_VOTAR);
            throw new NaoPodeVotarException();
        }

        return associado;
    }

    public Associado buscarAssociadoPorID(Long id) {
        return associadoRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException("Associado não encontrada."));
    }
}

