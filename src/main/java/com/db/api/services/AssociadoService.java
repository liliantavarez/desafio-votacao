package com.db.api.services;

import com.db.api.client.response.ConsultaCPFResponse;
import com.db.api.dtos.AssociadoDto;
import com.db.api.dtos.request.AssociadoRequest;
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

    public AssociadoDto cadastrarAssociado(@Valid AssociadoRequest associadoRequest) {
        verificaSeAssociadoJaEstaCadastrado(associadoRequest);

        Associado associadoCadastrado = associadoRepository.save(new Associado(associadoRequest.getNome(), associadoRequest.getCpf()));

        return new AssociadoDto(associadoCadastrado.getId(), associadoCadastrado.getNome(), associadoCadastrado.getCpf());
    }

    private void verificaSeAssociadoJaEstaCadastrado(AssociadoRequest associadoRequest) {
        if (associadoCadastrado(associadoRequest.getCpf())) {
            throw new AssociadoJaCadastradoException();
        }
    }

    private boolean associadoCadastrado(String cpfAssociado) {
        return associadoRepository.existsAssociadoByCpf(cpfAssociado);
    }

    public Associado buscarAssociadoPorCPF(String cpfAssociado) {
        return associadoRepository.findByCpf(cpfAssociado).orElseThrow(() -> new RegistroNaoEncontradoException("Associado não encontrada."));
    }

    public Associado validarAssociadoParaVotacao(String cpfAssociado) {
        Associado associado = buscarAssociadoPorCPF(cpfAssociado);
        validarSituacaoCPF(associado.getCpf());

        return associado;
    }

    public AssociadoDto buscarAssociadoPorID(Long id) {
        Associado associado = associadoRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException("Associado não encontrada."));
        return new AssociadoDto(associado.getNome(), associado.getCpf());
    }

    private void validarSituacaoCPF(String cpf) {
        ConsultaCPFResponse consultaCPF = consultaCPFService.verificarSituacaoCPF(cpf);

        if (!Objects.equals(consultaCPF.getSituacao(), "Regular")) {
            throw new NaoPodeVotarException();
        }
    }
}

