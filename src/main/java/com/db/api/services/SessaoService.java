package com.db.api.services;

import com.db.api.dtos.SessaoDto;
import com.db.api.dtos.request.PautaRequestSessao;
import com.db.api.dtos.response.SessaoResponse;
import com.db.api.enums.ResultadoSessao;
import com.db.api.enums.StatusSessao;
import com.db.api.enums.VotoEnum;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.exceptions.SessaoEncerradaException;
import com.db.api.models.Pauta;
import com.db.api.models.Sessao;
import com.db.api.repositories.SessaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;
    @PersistenceContext
    EntityManager entityManager;

    public SessaoDto iniciarSessaoVotacao(@Valid String pautaTitulo, @Valid LocalDateTime dataEncerramento) {
        Pauta pauta = buscarPauta(pautaTitulo);

        if (pauta == null) {
            throw new RegistroNaoEncontradoException("Pauta não encontrada.");
        }

        Sessao sessaoIniciada = sessaoRepository.save(criarSessao(pauta, dataEncerramento));
        PautaRequestSessao pautaRequestSessao = new PautaRequestSessao(sessaoIniciada.getPauta().getTitulo());

        return new SessaoDto(sessaoIniciada.getId(), pautaRequestSessao, sessaoIniciada.getDataEncerramento());
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

    public Sessao validarSessao(long sessaoId) {
        Sessao sessao = buscarSessaoPorID(sessaoId);

        if (sessao.getDataEncerramento().isBefore(LocalDateTime.now()) && sessao.getStatusSessao() == StatusSessao.ABERTA) {
            encerrarSessao(sessao);
        }

        if (sessao.getStatusSessao() == StatusSessao.ENCERRADA) {
            throw new SessaoEncerradaException();
        }

        return sessao;
    }

    public void encerrarSessao(Sessao sessao) {
        sessao.setStatusSessao(StatusSessao.ENCERRADA);
        sessaoRepository.save(sessao);
    }

    public SessaoResponse contabilizarVotos(Long sessaoId) {
        Sessao sessao = buscarSessaoPorID(sessaoId);

        if (sessao == null) {
            throw new RegistroNaoEncontradoException("Sessão não encontrada.");
        }

        List<VotoEnum> votos = sessaoRepository.buscarVotosDaSessao(sessaoId);

        Map<VotoEnum, Long> contagemVotos = votos.stream().collect(Collectors.groupingBy(voto -> voto, Collectors.counting()));

        sessao.setResultadoSessao(definirResultadoSessao(contagemVotos));

        return new SessaoResponse(sessao.getPauta(), sessao.getDataEncerramento(), sessao.getResultadoSessao());
    }

    private ResultadoSessao definirResultadoSessao(Map<VotoEnum, Long> contagemVotos) {
        long votosSim = contagemVotos.getOrDefault(VotoEnum.SIM, 0L);
        long votosNao = contagemVotos.getOrDefault(VotoEnum.NAO, 0L);

        switch (Long.compare(votosSim, votosNao)) {
            case 1:
                return ResultadoSessao.APROVADA;
            case -1:
                return ResultadoSessao.REPROVADA;
            default:
                return ResultadoSessao.INDEFINIDA;
        }
    }

    public Sessao buscarSessaoPorID(Long sessaoId) {
        return sessaoRepository.findById(sessaoId).orElseThrow(() -> new RegistroNaoEncontradoException("Sessão não encontrada."));
    }
}

