package com.db.api.services;

import com.db.api.dtos.response.SessaoResponse;
import com.db.api.enums.ResultadoSessao;
import com.db.api.enums.StatusSessao;
import com.db.api.enums.VotoEnum;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.exceptions.SessaoEncerradaException;
import com.db.api.models.Pauta;
import com.db.api.models.Sessao;
import com.db.api.repositories.SessaoRepository;
import com.db.api.repositories.VotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;
    private final VotoRepository votoRepository;

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
    @Transactional
    public SessaoResponse contabilizarVotos(Long id) {
        Sessao sessao = buscarSessaoPorId(id);
        int votosSim = votoRepository.countVotoByVotoEnumAndAndSessaoId(VotoEnum.SIM, id);
        int votosNao = votoRepository.countVotoByVotoEnumAndAndSessaoId(VotoEnum.NAO, id);

        definirResultadoSessao(sessao, votosSim, votosNao);

        return new SessaoResponse(sessao.getPauta(), sessao.getDataEncerramento(), sessao.getResultadoSessao());
    }
    private Sessao buscarSessaoPorId(Long id) {
        Optional<Sessao> sessaoOptional = sessaoRepository.findById(id);
        return sessaoOptional.orElseThrow(() -> new RegistroNaoEncontradoException("A sessão requerida não foi encontrada!"));
    }

    private void definirResultadoSessao(Sessao sessao, int votosSim, int votosNao) {
        if (votosSim > votosNao) {
            sessao.setResultadoSessao(ResultadoSessao.APROVADA);
        } else if (votosNao > votosSim) {
            sessao.setResultadoSessao(ResultadoSessao.REPROVADA);
        } else {
            sessao.setResultadoSessao(ResultadoSessao.INDEFINIDA);
        }
    }
}

