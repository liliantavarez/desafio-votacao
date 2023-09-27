package com.db.api.services;

import com.db.api.enums.StatusSessao;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.exceptions.SessaoEncerradaException;
import com.db.api.models.Pauta;
import com.db.api.models.Sessao;
import com.db.api.repositories.SessaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;

    @Transactional
    public Sessao iniciarSessaoVotacao(String pautaTitulo, LocalDateTime dataEncerramento) {
        Pauta pauta = buscarPauta(pautaTitulo);
        Sessao sessao = criarSessao(pauta, dataEncerramento);

        if (pauta == null) {
            throw new RegistroNaoEncontradoException("Pauta não encontrada.");
        }

        return sessaoRepository.save(sessao);
    }

    private Pauta buscarPauta(String pautaTitulo) {
        return pautaService.buscarPauta(pautaTitulo);
    }

    private Sessao criarSessao(Pauta pauta, LocalDateTime dataEncerramento) {
        Sessao sessao = new Sessao(pauta);

        if (dataEncerramento != null) {
            sessao.setDataEncerramento(dataEncerramento);
        }

        return sessao;
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

    @Transactional
    public void encerrarSessao(Sessao sessao) {
        sessao.setStatusSessao(StatusSessao.ENCERRADA);
        sessaoRepository.save(sessao);
    }
}

