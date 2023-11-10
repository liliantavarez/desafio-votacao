package com.db.api.services;

import com.db.api.dtos.PautaDto;
import com.db.api.dtos.request.PautaRequest;
import com.db.api.exceptions.ParametrosInvalidosException;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.models.Pauta;
import com.db.api.repositories.PautaRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;

    public PautaDto criarNovaPauta(PautaRequest pautaRequest) {
        if (!StringUtils.isNotBlank(pautaRequest.getTitulo())) {
            throw new ParametrosInvalidosException("Por favor, informe o titulo da pauta!");
        }

        Pauta pautaCadastrada = pautaRepository.save(new Pauta(pautaRequest.getTitulo(), pautaRequest.getDescricao()));
        return new PautaDto(pautaCadastrada.getId(), pautaCadastrada.getTitulo(), pautaCadastrada.getDescricao());
    }

    public Pauta buscarPauta(String pautaTitulo) {
        return pautaRepository.findByTitulo(pautaTitulo).orElseThrow(() -> new RegistroNaoEncontradoException("A pauta requerida não foi encontrado!"));
    }

    public Pauta buscarPautaPorID(Long id) {
        return pautaRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException("A pauta requerida não foi encontrado!"));
    }
}

